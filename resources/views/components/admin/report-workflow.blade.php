@props(['report', 'staff'])

<div class="grid gap-6 xl:grid-cols-3">
    <section class="space-y-6 xl:col-span-2">
        <x-report-photo-gallery :report="$report" />

        <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
            <div class="flex flex-wrap justify-between gap-3">
                <div><h2 class="text-2xl font-bold text-navy">{{ $report->title }}</h2><p class="mt-1 text-sm text-navy/55">Submitted {{ $report->created_at->format('M d, Y g:i A') }}</p></div>
                <div class="flex flex-wrap gap-2"><x-admin.badge :value="$report->validation_status" /><x-admin.badge :value="$report->priority" /><x-admin.badge :value="$report->status" /></div>
            </div>
            <p class="mt-6 whitespace-pre-line text-navy/75">{{ $report->description }}</p>
            <dl class="mt-6 grid gap-4 sm:grid-cols-2">
                <div><dt class="text-xs uppercase text-navy/45">Reporter</dt><dd>{{ $report->resident->first_name }} {{ $report->resident->last_name }} · {{ $report->resident->phone_number }}</dd></div>
                <div><dt class="text-xs uppercase text-navy/45">Location</dt><dd>Purok {{ $report->purok->name }} @if($report->latitude) · {{ $report->latitude }}, {{ $report->longitude }} @endif</dd></div>
                <div><dt class="text-xs uppercase text-navy/45">Category</dt><dd>{{ $report->category->name }}</dd></div>
                <div><dt class="text-xs uppercase text-navy/45">Assigned staff</dt><dd>{{ $report->assignedStaff?->first_name ?? 'Unassigned' }}</dd></div>
            </dl>
            @if($report->validation_status === 'rejected')<div class="mt-5 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-800"><strong>Rejection reason:</strong> {{ $report->rejection_reason }}</div>@endif
        </div>

        <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
            <h3 class="font-bold">Notes and updates</h3>
            <form method="POST" action="{{ route('admin.reports.notes',$report) }}" class="mt-4">@csrf<textarea name="body" required rows="3" class="w-full rounded-xl border border-navy/15 p-3" placeholder="Add an administrative note"></textarea><label class="mt-3 flex items-center gap-2 text-sm"><input type="checkbox" name="is_resident_visible" value="1"> Visible to resident</label><button class="mt-3 rounded-xl bg-teal px-4 py-2 font-semibold text-white">Add note</button></form>
            <div class="mt-6 space-y-3">@forelse($report->notes->sortByDesc('created_at') as $note)<div class="rounded-xl bg-slate-50 p-4 text-sm"><p>{{ $note->body }}</p><p class="mt-2 text-xs text-navy/45">{{ $note->is_resident_visible ? 'Resident-visible update' : 'Internal note' }} · {{ $note->created_at->diffForHumans() }}</p></div>@empty<p class="text-sm text-navy/50">No notes yet.</p>@endforelse</div>
        </div>
    </section>

    <aside class="space-y-6">
        @if($report->validation_status === 'rejected')
            <div class="rounded-2xl border border-red-200 bg-white p-6 shadow-sm ring-1 ring-navy/5">
                <div class="flex items-center justify-between gap-3"><h3 class="font-bold">Report validation</h3><x-admin.badge value="rejected" /></div>
                <p class="mt-4 text-sm text-navy/60">This rejection is final. The saved reason cannot be edited.</p>
                <div class="mt-4 rounded-xl bg-red-50 p-4 text-sm text-red-800"><p class="text-xs font-bold uppercase tracking-wide">Rejection reason</p><p class="mt-2 whitespace-pre-line">{{ $report->rejection_reason }}</p></div>
            </div>
        @else
        <form method="POST" action="{{ route('admin.reports.update',$report) }}" class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
            @csrf @method('PATCH')
            <div class="flex items-center justify-between gap-3"><h3 class="font-bold">Report validation</h3><x-admin.badge :value="$report->validation_status" /></div>
            @if($report->validation_status === 'validated')
                <div class="mt-4 rounded-xl border border-emerald-200 bg-emerald-50 p-4 text-sm text-emerald-800">Validated {{ $report->validated_at?->format('M d, Y g:i A') }}. This report may now be assigned to staff.</div>
            @else
                <p class="mt-3 text-sm text-navy/60">Review the report details, photo evidence, and location before allowing barangay action.</p>
            @endif
            <label class="mt-4 block text-sm font-semibold">Rejection reason<textarea name="rejection_reason" rows="3" minlength="10" maxlength="2000" class="mt-1 w-full rounded-xl border border-navy/15 p-2.5" placeholder="Required only when rejecting">{{ old('rejection_reason', $report->rejection_reason) }}</textarea></label>
            @error('rejection_reason')<p class="mt-1 text-sm text-red-600">{{ $message }}</p>@enderror
            <div class="mt-4 grid gap-3 sm:grid-cols-2 xl:grid-cols-1 2xl:grid-cols-2">
                <button name="validation_status" value="validated" class="rounded-xl bg-teal px-4 py-3 font-semibold text-white" onclick="return confirm('Validate this report and allow staff assignment?')">Validate report</button>
                <button name="validation_status" value="rejected" class="rounded-xl border border-red-300 bg-red-50 px-4 py-3 font-semibold text-red-700" onclick="return confirm('Reject this report? Any current assignment will be removed.')">Reject report</button>
            </div>
        </form>
        @endif

        @if($report->validation_status === 'validated')
            <form method="POST" action="{{ route('admin.reports.update',$report) }}" class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5" onsubmit="return confirm('Confirm this report update?')">
                @csrf @method('PATCH')
                <h3 class="font-bold">Assign and manage action</h3>
                <label class="mt-4 block text-sm font-semibold">Assigned staff<select name="assigned_staff_id" class="mt-1 w-full rounded-xl border border-navy/15 p-2.5"><option value="">Unassigned</option>@foreach($staff as $member)<option value="{{ $member->id }}" @selected($report->assigned_staff_id===$member->id)>{{ $member->first_name }} {{ $member->last_name }}</option>@endforeach</select></label>
                <label class="mt-4 block text-sm font-semibold">Priority<select name="priority" class="mt-1 w-full rounded-xl border border-navy/15 p-2.5">@foreach(['low','normal','high','urgent'] as $v)<option @selected($report->priority===$v)>{{ $v }}</option>@endforeach</select></label>
                <label class="mt-4 block text-sm font-semibold">Status<select name="status" class="mt-1 w-full rounded-xl border border-navy/15 p-2.5">@foreach(['pending','in_progress','resolved'] as $v)<option @selected($report->status===$v)>{{ str($v)->replace('_',' ')->title() }}</option>@endforeach</select></label>
                <label class="mt-4 block text-sm font-semibold">Resolution date<input type="datetime-local" name="resolution_date" value="{{ $report->resolved_at?->format('Y-m-d\TH:i') }}" class="mt-1 w-full rounded-xl border border-navy/15 p-2.5"></label>
                <label class="mt-4 block text-sm font-semibold">Resolution notes<textarea name="resolution_notes" class="mt-1 w-full rounded-xl border border-navy/15 p-2.5">{{ $report->resolution_notes }}</textarea></label>
                <button class="mt-5 w-full rounded-xl bg-teal px-4 py-3 font-semibold text-white">Save changes</button>
            </form>
        @else
            <div class="rounded-2xl border border-amber-200 bg-amber-50 p-6 text-amber-900 shadow-sm"><h3 class="font-bold">Staff action locked</h3><p class="mt-2 text-sm">Validate this report before assigning staff, changing its action status, or resolving it.</p></div>
        @endif
    </aside>
</div>
