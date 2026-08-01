@props([
    'title',
    'icon' => 'shield-check',
    'accent' => 'teal',
    'badge' => null,
])

@php
    $iconWrapClasses = match ($accent) {
        'sky' => 'bg-sky/10 text-sky',
        'navy' => 'bg-navy/10 text-navy',
        'red-urgent' => 'bg-red-urgent/10 text-red-urgent',
        default => 'bg-teal/10 text-teal',
    };
@endphp

<article class="flex h-full flex-col gap-4 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
    <div class="flex items-start justify-between gap-3">
        <span class="inline-flex h-12 w-12 shrink-0 items-center justify-center rounded-xl {{ $iconWrapClasses }}">
            <x-icon :name="$icon" class="h-6 w-6" />
        </span>

        @isset($badge)
            <span class="inline-flex items-center rounded-full bg-amber/15 px-3 py-1 text-xs font-semibold text-navy">
                {{ $badge }}
            </span>
        @endisset
    </div>

    <div>
        <h3 class="text-lg font-semibold text-navy sm:text-xl">{{ $title }}</h3>
        <p class="mt-2 text-[15px] leading-relaxed text-navy/70">{{ $slot }}</p>
    </div>
</article>
