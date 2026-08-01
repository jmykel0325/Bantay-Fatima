<?php

namespace Tests\Feature;

use App\Models\AuditLog;
use App\Models\Purok;
use App\Models\Report;
use App\Models\ReportAssignment;
use App\Models\ReportCategory;
use App\Models\ReportStatusHistory;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Facades\Storage;
use Tests\TestCase;

class AdminManagementTest extends TestCase
{
    use RefreshDatabase;

    public function test_only_admin_can_access_admin_dashboard(): void
    {
        $this->actingAs($this->user('admin'))->get(route('admin.dashboard'))->assertOk();
        $this->actingAs($this->user('staff', 'staff@example.com', '09171234568'))->get(route('admin.dashboard'))->assertForbidden();
        $this->actingAs($this->user('resident', 'resident2@example.com', '09171234569'))->get(route('admin.dashboard'))->assertForbidden();
    }

    public function test_dashboard_displays_database_report_statistics(): void
    {
        $admin = $this->user('admin');
        $this->report(['status' => 'pending']);
        $this->report(['status' => 'resolved', 'reference_number' => 'BF-000002', 'resolved_at' => now()]);

        $this->actingAs($admin)->get(route('admin.dashboard'))->assertOk()->assertSee('Total Reports')->assertSee('Pending Reports')->assertSee('Resolved Reports');
    }

    public function test_report_filters_and_admin_can_assign_and_update_status(): void
    {
        $admin = $this->user('admin');
        $staff = $this->user('staff', 'staff@example.com', '09171234568');
        $report = $this->report(['title' => 'Blocked drainage']);

        $this->actingAs($admin)->get(route('admin.reports.index', ['q' => 'Blocked']))->assertOk()->assertSee('Blocked drainage');
        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['assigned_staff_id' => $staff->id, 'status' => 'in_progress'])->assertSessionHasErrors(['assigned_staff_id', 'status']);
        $this->assertNull($report->fresh()->assigned_staff_id);

        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['validation_status' => 'validated'])->assertRedirect();
        $this->assertDatabaseHas('reports', ['id' => $report->id, 'validation_status' => 'validated']);
        $this->assertNotNull($report->fresh()->validated_at);

        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['assigned_staff_id' => $staff->id, 'priority' => 'urgent', 'status' => 'in_progress'])->assertRedirect();

        $this->assertDatabaseHas('reports', ['id' => $report->id, 'assigned_staff_id' => $staff->id, 'priority' => 'urgent', 'status' => 'in_progress']);
        $this->assertDatabaseHas(ReportAssignment::class, ['report_id' => $report->id, 'staff_id' => $staff->id]);
        $this->assertDatabaseHas(ReportStatusHistory::class, ['report_id' => $report->id, 'to_status' => 'in_progress']);
        $this->assertDatabaseHas(AuditLog::class, ['action' => 'report.updated', 'entity_id' => $report->id]);
    }

    public function test_rejecting_a_report_requires_a_reason_and_removes_staff_assignment(): void
    {
        $admin = $this->user('admin');
        $staff = $this->user('staff', 'staff@example.com', '09171234568');
        $report = $this->report(['validation_status' => 'validated', 'validated_at' => now(), 'assigned_staff_id' => $staff->id]);

        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['validation_status' => 'rejected'])->assertSessionHasErrors('rejection_reason');
        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['validation_status' => 'rejected', 'rejection_reason' => 'The submitted evidence does not match the reported concern.'])->assertRedirect();

        $this->assertDatabaseHas('reports', ['id' => $report->id, 'validation_status' => 'rejected', 'assigned_staff_id' => null, 'status' => 'rejected', 'rejection_reason' => 'The submitted evidence does not match the reported concern.']);
        $this->assertDatabaseHas('notifications', ['notifiable_id' => $report->resident_id]);

        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['validation_status' => 'validated'])->assertSessionHasErrors('validation_status');
        $this->actingAs($admin)->patch(route('admin.reports.update', $report), ['validation_status' => 'rejected', 'rejection_reason' => 'A different rejection reason that must not replace the original.'])->assertSessionHasErrors('validation_status');
        $this->assertSame('The submitted evidence does not match the reported concern.', $report->fresh()->rejection_reason);
    }

    public function test_admin_can_create_staff_but_duplicate_email_is_rejected(): void
    {
        $admin = $this->user('admin');
        $payload = ['first_name'=>'Ana','last_name'=>'Santos','email'=>'staff@example.com','phone_number'=>'0917 555 0101','password'=>'Temporary1','password_confirmation'=>'Temporary1','status'=>'active'];
        $this->actingAs($admin)->post(route('admin.users.staff.store'), $payload)->assertRedirect();
        $this->assertDatabaseHas('users', ['email'=>'staff@example.com','phone_number'=>'+639175550101','role'=>'staff','must_change_password'=>true,'created_by'=>$admin->id]);
        $this->actingAs($admin)->post(route('admin.users.staff.store'), [...$payload, 'phone_number'=>'0917 555 0102'])->assertSessionHasErrors('email');
    }

    public function test_admin_cannot_suspend_own_account(): void
    {
        $admin = $this->user('admin');
        $this->actingAs($admin)->patch(route('admin.users.status', $admin), ['status'=>'suspended'])->assertStatus(422);
        $this->assertSame('active', $admin->fresh()->status);
    }

    public function test_admin_and_only_assigned_staff_can_view_report_photo_evidence(): void
    {
        Storage::fake('public');
        $admin = $this->user('admin');
        $assigned = $this->user('staff', 'assigned@example.com', '09171234568');
        $unassigned = $this->user('staff', 'unassigned@example.com', '09171234569');
        $report = $this->report(['assigned_staff_id' => $assigned->id]);
        Storage::disk('public')->put('reports/1/evidence.jpg', 'jpeg-content');
        $photo = $report->photos()->create(['path' => 'reports/1/evidence.jpg', 'original_name' => 'evidence.jpg', 'mime_type' => 'image/jpeg', 'size' => 12]);

        $this->actingAs($admin)->get(route('admin.reports.show', $report))->assertOk()->assertSee('Photo Evidence')->assertSee('evidence.jpg');
        $this->actingAs($admin)->get(route('admin.report-photos.show', $photo))->assertOk()->assertHeader('content-type', 'image/jpeg');
        $this->actingAs($admin)->get(route('admin.report-media.show', ['path' => $photo->path]))->assertOk()->assertHeader('content-type', 'image/jpeg');
        $this->actingAs($assigned)->get(route('admin.report-photos.show', $photo))->assertForbidden();
        $this->actingAs($unassigned)->get(route('admin.report-media.show', ['path' => $photo->path]))->assertForbidden();
    }

    private function user(string $role, string $email = 'admin@example.com', string $phone = '09171234567'): User
    {
        return User::factory()->create(['role'=>$role,'email'=>$email,'phone_number'=>$phone,'status'=>'active']);
    }

    private function report(array $attributes = []): Report
    {
        static $residentSequence = 0;
        $residentSequence++;
        $resident = $this->user('resident', "resident{$residentSequence}@example.com", '0918'.str_pad((string) $residentSequence, 7, '0', STR_PAD_LEFT));
        return Report::create(array_merge(['reference_number'=>'BF-000001','resident_id'=>$resident->id,'category_id'=>ReportCategory::first()->id,'purok_id'=>Purok::first()->id,'title'=>'Community concern','description'=>'A resident-submitted community concern.','priority'=>'normal','status'=>'pending'], $attributes));
    }
}
