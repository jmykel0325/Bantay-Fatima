@props(['report'])
<section {{ $attributes->class('rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5') }}>
    <div class="flex flex-wrap items-center justify-between gap-2">
        <div><h2 class="text-lg font-bold text-navy">Photo Evidence</h2><p class="mt-1 text-sm text-navy/55">Review the resident’s original attachments when validating this report.</p></div>
        <span class="rounded-full bg-teal/10 px-3 py-1 text-xs font-semibold text-teal">{{ $report->photos->count() }} {{ Str::plural('photo', $report->photos->count()) }}</span>
    </div>
    @forelse($report->photos as $photo)
        @if($loop->first)<div class="mt-5 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">@endif
        <a href="{{ route('admin.report-photos.show', $photo) }}" target="_blank" rel="noopener" class="group overflow-hidden rounded-xl border border-navy/10 bg-surface focus:ring-2 focus:ring-teal">
            <img src="{{ route('admin.report-photos.show', $photo) }}" alt="Evidence photo {{ $loop->iteration }} for {{ $report->reference_number }}" class="h-52 w-full object-cover transition group-hover:scale-[1.02]">
            <div class="flex items-center justify-between gap-2 p-3 text-xs"><span class="truncate text-navy/60">{{ $photo->original_name ?: 'Evidence photo '.$loop->iteration }}</span><span class="font-semibold text-teal">Open full size</span></div>
        </a>
        @if($loop->last)</div>@endif
    @empty
        <div class="mt-5 rounded-xl border border-dashed border-navy/15 p-8 text-center text-sm text-navy/50">No photo evidence is attached to this report.</div>
    @endforelse
</section>
