@props([
    'issue',
    'location',
    'status' => 'pending',
    'date' => 'Sample date',
])

@php
    [$statusLabel, $badgeClasses, $dotClasses] = match ($status) {
        'in-progress' => ['In Progress', 'bg-sky/15 text-navy', 'bg-sky'],
        'resolved' => ['Resolved', 'bg-resolved/15 text-navy', 'bg-resolved'],
        'rejected' => ['Rejected', 'bg-red-urgent/15 text-navy', 'bg-red-urgent'],
        default => ['Pending', 'bg-amber/15 text-navy', 'bg-amber'],
    };
@endphp

<article class="landing-card flex h-full flex-col gap-4 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
    <span class="inline-flex w-fit items-center rounded-full bg-navy/5 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-navy/60">
        Sample Report
    </span>

    <div>
        <h3 class="text-lg font-semibold text-navy">{{ $issue }}</h3>
        <p class="mt-1 flex items-center gap-1.5 text-sm text-navy/70">
            <x-icon name="pin" class="h-4 w-4 shrink-0" />
            {{ $location }}
        </p>
    </div>

    <div class="flex flex-wrap items-center gap-3">
        <span class="inline-flex items-center gap-2 rounded-full {{ $badgeClasses }} px-3 py-1 text-sm font-semibold">
            <span class="h-2 w-2 rounded-full {{ $dotClasses }}" aria-hidden="true"></span>
            {{ $statusLabel }}
        </span>
        <span class="flex items-center gap-1.5 text-xs text-navy/50">
            <x-icon name="calendar" class="h-3.5 w-3.5" />
            {{ $date }}
        </span>
    </div>

    <button type="button" class="mt-auto inline-flex min-h-[44px] w-full items-center justify-center rounded-xl border border-primary bg-white px-4 text-sm font-semibold text-primary transition-colors hover:bg-primary/5">
        View Details
    </button>
</article>
