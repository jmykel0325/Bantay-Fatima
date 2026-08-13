@props([
    'title',
    'type' => 'info',
    'date' => 'Sample date',
])

@php
    [$typeLabel, $badgeClasses, $iconWrapClasses, $icon] = match ($type) {
        'advisory' => ['Safety Advisory', 'bg-amber/15 text-navy', 'bg-amber/10 text-amber', 'shield-check'],
        'notice' => ['Community Notice', 'bg-teal/15 text-navy', 'bg-teal/10 text-teal', 'megaphone'],
        default => ['Announcement', 'bg-sky/15 text-navy', 'bg-sky/10 text-sky', 'megaphone'],
    };
@endphp

<article class="landing-card flex h-full flex-col gap-4 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
    <div class="flex items-center justify-between gap-3">
        <span class="inline-flex h-10 w-10 items-center justify-center rounded-xl {{ $iconWrapClasses }}">
            <x-icon :name="$icon" class="h-5 w-5" />
        </span>
        <span class="inline-flex items-center rounded-full {{ $badgeClasses }} px-3 py-1 text-xs font-semibold">
            {{ $typeLabel }}
        </span>
    </div>

    <div>
        <span class="text-xs font-semibold uppercase tracking-wide text-navy/50">Sample Content</span>
        <h3 class="mt-1 text-lg font-semibold text-navy">{{ $title }}</h3>
        <p class="mt-2 text-[15px] leading-relaxed text-navy/70">{{ $slot }}</p>
    </div>

    <span class="mt-auto flex items-center gap-1.5 text-xs text-navy/50">
        <x-icon name="calendar" class="h-3.5 w-3.5" />
        {{ $date }}
    </span>
</article>
