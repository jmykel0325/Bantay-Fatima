<?php

namespace Tests\Feature;

use App\Mail\VerificationCodeMail;
use App\Models\EmailVerificationCode;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Tests\TestCase;

class EmailAuthenticationTest extends TestCase
{
    use RefreshDatabase;

    public function test_resident_cannot_login_through_administrator_form(): void
    {
        User::factory()->create(['email' => 'resident@example.com', 'password' => Hash::make('Password1'), 'role' => 'resident', 'status' => 'active']);
        $this->post(route('admin.login.store'), ['login' => 'resident@example.com', 'password' => 'Password1'])->assertSessionHasErrors(['login' => 'The provided administrator credentials are incorrect.']);
        $this->assertGuest();
    }

    public function test_admin_is_redirected_to_admin_dashboard_after_login(): void
    {
        $admin = User::factory()->create([
            'email' => 'admin@example.com',
            'password' => Hash::make('Password1'),
            'role' => 'admin',
        ]);

        $this->post(route('admin.login.store'), [
            'login' => 'admin@example.com',
            'password' => 'Password1',
        ])->assertRedirect(route('admin.dashboard'));

        $this->actingAs($admin)->get(route('admin.dashboard'))->assertOk();
    }

    public function test_resident_cannot_access_admin_dashboard(): void
    {
        $resident = User::factory()->create(['role' => 'resident']);

        $this->actingAs($resident)->get(route('admin.dashboard'))->assertForbidden();
    }

    public function test_guest_is_redirected_to_hidden_admin_login_from_protected_admin_page(): void
    {
        $this->get(route('admin.dashboard'))->assertRedirect(route('admin.login'));
    }

    public function test_staff_cannot_login_through_administrator_form(): void
    {
        User::factory()->create(['email' => 'staff@example.com', 'password' => Hash::make('Password1'), 'role' => 'staff', 'status' => 'active']);
        $this->post(route('admin.login.store'), ['login' => 'staff@example.com', 'password' => 'Password1'])->assertSessionHasErrors(['login' => 'The provided administrator credentials are incorrect.']);
        $this->assertGuest();
    }

    public function test_inactive_admin_cannot_login(): void
    {
        User::factory()->create(['email' => 'inactive@example.com', 'password' => Hash::make('Password1'), 'role' => 'admin', 'status' => 'suspended']);
        $this->post(route('admin.login.store'), ['login' => 'inactive@example.com', 'password' => 'Password1'])->assertSessionHasErrors(['login' => 'The provided administrator credentials are incorrect.']);
        $this->assertGuest();
    }

    public function test_login_uses_a_generic_error(): void
    {
        User::factory()->create(['email' => 'admin@example.com', 'password' => Hash::make('Password1'), 'role' => 'admin', 'status' => 'active']);
        $this->from(route('admin.login'))->post(route('admin.login.store'), ['login' => 'admin@example.com', 'password' => 'wrong'])
            ->assertSessionHasErrors(['login' => 'The provided administrator credentials are incorrect.']);
    }

    public function test_registration_sends_code_without_creating_user(): void
    {
        Mail::fake();
        $this->postJson('/api/auth/register/send-code', $this->registrationData())->assertOk()->assertJsonPath('email', 'resident@example.com');
        Mail::assertSent(VerificationCodeMail::class, fn ($mail) => $mail->hasTo('resident@example.com'));
        $this->assertDatabaseCount('users', 0);
    }

    public function test_correct_email_code_creates_user(): void
    {
        EmailVerificationCode::create(['email' => 'resident@example.com', 'purpose' => 'registration', 'code_hash' => Hash::make('123456'), 'payload' => $this->pendingPayload(), 'expires_at' => now()->addMinutes(10)]);
        $this->postJson('/api/auth/register/verify', ['email' => 'resident@example.com', 'code' => '123456'])->assertCreated();
        $this->assertDatabaseHas('users', ['email' => 'resident@example.com', 'phone_number' => '+639171234567']);
    }

    public function test_incorrect_email_code_does_not_create_user(): void
    {
        EmailVerificationCode::create(['email' => 'resident@example.com', 'purpose' => 'registration', 'code_hash' => Hash::make('123456'), 'payload' => $this->pendingPayload(), 'expires_at' => now()->addMinutes(10)]);
        $this->postJson('/api/auth/register/verify', ['email' => 'resident@example.com', 'code' => '999999'])->assertStatus(422)->assertJsonValidationErrors(['code']);
        $this->assertDatabaseCount('users', 0);
    }

    public function test_password_reset_code_updates_password(): void
    {
        $user = User::factory()->create(['email' => 'resident@example.com', 'password' => Hash::make('OldPassword1')]);
        EmailVerificationCode::create(['email' => $user->email, 'purpose' => 'password_reset', 'code_hash' => Hash::make('654321'), 'payload' => ['user_id' => $user->id], 'expires_at' => now()->addMinutes(10)]);
        $this->postJson('/api/auth/forgot-password/reset', ['email' => $user->email, 'code' => '654321', 'password' => 'NewPassword1', 'password_confirmation' => 'NewPassword1'])->assertOk();
        $this->assertTrue(Hash::check('NewPassword1', $user->fresh()->password));
    }

    public function test_api_login_returns_sanctum_token(): void
    {
        User::factory()->create(['email' => 'resident@example.com', 'password' => Hash::make('Password1')]);
        $this->postJson('/api/auth/login', ['email' => 'resident@example.com', 'password' => 'Password1', 'device_name' => 'Test Android'])->assertOk()->assertJsonStructure(['success','message','user', 'token', 'token_type'])->assertJsonPath('user.role','resident');
    }

    private function registrationData(): array
    {
        return ['first_name' => 'Jose', 'middle_name' => 'Ñolasco', 'last_name' => 'Dela-Cruz', 'suffix' => null, 'email' => 'resident@example.com', 'phone_number' => '0917 123 4567', 'password' => 'Password1', 'password_confirmation' => 'Password1', 'terms' => true];
    }

    private function pendingPayload(): array
    {
        return ['first_name' => 'Jose', 'middle_name' => 'Ñolasco', 'last_name' => 'Dela-Cruz', 'suffix' => null, 'email' => 'resident@example.com', 'phone_number' => '+639171234567', 'password' => Hash::make('Password1')];
    }
}
