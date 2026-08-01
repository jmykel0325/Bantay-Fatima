<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreReportNoteRequest;
use App\Http\Requests\Admin\UpdateReportRequest;
use App\Models\Purok;
use App\Models\Report;
use App\Models\ReportAssignment;
use App\Models\ReportCategory;
use App\Models\ReportNote;
use App\Models\ReportStatusHistory;
use App\Models\User;
use App\Notifications\ReportActivityNotification;
use App\Services\AuditLogger;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\View\View;
use Symfony\Component\HttpFoundation\StreamedResponse;

class AdminReportController extends Controller
{
    public function index(Request $request): View
    {
        $reports = $this->filtered($request)->with(['resident','category','purok','assignedStaff'])
            ->when($request->sort === 'oldest', fn ($q) => $q->oldest(), fn ($q) => $q->latest())->paginate(15)->withQueryString();
        return view('admin.reports.index', compact('reports') + $this->filters());
    }

    public function show(Report $report): View
    {
        $report->load(['resident','category','purok','assignedStaff','photos','notes','statusHistories','assignments']);
        return view('admin.reports.show', compact('report') + ['staff' => User::where('role','staff')->where('status','active')->orderBy('last_name')->get()]);
    }

    public function update(UpdateReportRequest $request, Report $report, AuditLogger $audit): RedirectResponse
    {
        $data = $request->validated();
        $old = $report->only(['validation_status','rejection_reason','validated_at','status','priority','assigned_staff_id','resolved_at','resolution_notes']);
        DB::transaction(function () use ($data, $report, $old, $audit) {
            if (isset($data['validation_status'])) {
                if ($data['validation_status'] === 'validated') {
                    $data['validated_at'] = now();
                    $data['rejection_reason'] = null;
                } else {
                    $data['validated_at'] = null;
                    $data['assigned_staff_id'] = null;
                    $data['status'] = 'rejected';
                    $data['resolved_at'] = null;
                    $data['resolution_notes'] = null;
                }
            }
            if (array_key_exists('assigned_staff_id', $data) && (int) $report->assigned_staff_id !== (int) $data['assigned_staff_id']) {
                abort_if($data['assigned_staff_id'] && ! User::whereKey($data['assigned_staff_id'])->where('role','staff')->where('status','active')->exists(), 422);
                ReportAssignment::where('report_id',$report->id)->whereNull('unassigned_at')->update(['unassigned_at'=>now()]);
                if ($data['assigned_staff_id']) ReportAssignment::create(['report_id'=>$report->id,'staff_id'=>$data['assigned_staff_id'],'assigned_by'=>auth()->id(),'assigned_at'=>now()]);
            }
            if (isset($data['status']) && $data['status'] !== $report->status) ReportStatusHistory::create(['report_id'=>$report->id,'changed_by'=>auth()->id(),'from_status'=>$report->status,'to_status'=>$data['status'],'notes'=>$data['resolution_notes'] ?? null,'created_at'=>now()]);
            if (($data['status'] ?? null) === 'resolved') $data['resolved_at'] = $data['resolution_date'];
            elseif (isset($data['status']) && $data['status'] !== 'resolved') { $data['resolved_at'] = null; $data['resolution_notes'] = null; }
            unset($data['resolution_date']);
            $report->update($data);
            $fresh = $report->fresh();
            $audit->log('report.updated',$report,'Report validation, assignment, status, or priority updated.',$old,$fresh->only(array_keys($old)));
            if (isset($data['validation_status']) && $old['validation_status'] !== $data['validation_status']) {
                $validated = $data['validation_status'] === 'validated';
                $fresh->resident->notify(new ReportActivityNotification(
                    $fresh,
                    $validated ? 'Report validated' : 'Report rejected',
                    $validated
                        ? "Your report {$fresh->reference_number} was validated and is ready for barangay action."
                        : "Your report {$fresh->reference_number} was rejected. Review the reason in your report details."
                ));
            }
        });
        return back()->with('success','Report updated successfully.');
    }

    public function note(StoreReportNoteRequest $request, Report $report, AuditLogger $audit): RedirectResponse
    {
        $note = $report->notes()->create(['user_id'=>auth()->id(), ...$request->validated(), 'is_resident_visible'=>$request->boolean('is_resident_visible')]);
        $audit->log('report.note_created',$note,'Administrative report note added.',[],['resident_visible'=>$note->is_resident_visible]);
        return back()->with('success','Report note added successfully.');
    }

    public function export(Request $request): StreamedResponse
    {
        return response()->streamDownload(function () use ($request) { $out=fopen('php://output','w'); fputcsv($out,['Reference','Title','Reporter','Category','Purok','Submitted','Priority','Status','Staff']); $this->filtered($request)->with(['resident','category','purok','assignedStaff'])->orderBy('id')->chunk(500,function($rows)use($out){foreach($rows as $r)fputcsv($out,[$r->reference_number,$r->title,trim($r->resident->first_name.' '.$r->resident->last_name),$r->category->name,$r->purok->name,$r->created_at,$r->priority,$r->status,$r->assignedStaff?->first_name]);}); fclose($out); }, 'bantay-fatima-reports-'.now()->format('Y-m-d').'.csv', ['Content-Type'=>'text/csv']);
    }

    private function filtered(Request $request)
    {
        return Report::query()->when($request->q, fn($q,$v)=>$q->where(fn($s)=>$s->where('reference_number','like',"%$v%")->orWhere('title','like',"%$v%")->orWhere('description','like',"%$v%")->orWhereHas('resident',fn($u)=>$u->where('first_name','like',"%$v%")->orWhere('last_name','like',"%$v%")->orWhere('phone_number','like',"%$v%"))))->when($request->status,fn($q,$v)=>$q->where('status',$v))->when($request->priority,fn($q,$v)=>$q->where('priority',$v))->when($request->category_id,fn($q,$v)=>$q->where('category_id',$v))->when($request->purok_id,fn($q,$v)=>$q->where('purok_id',$v))->when($request->staff_id,fn($q,$v)=>$q->where('assigned_staff_id',$v))->when($request->date_from,fn($q,$v)=>$q->whereDate('created_at','>=',$v))->when($request->date_to,fn($q,$v)=>$q->whereDate('created_at','<=',$v));
    }
    private function filters(): array { return ['categories'=>ReportCategory::where('is_active',true)->orderBy('name')->get(),'puroks'=>Purok::where('is_active',true)->orderBy('sort_order')->get(),'staff'=>User::where('role','staff')->where('status','active')->orderBy('last_name')->get()]; }
}
