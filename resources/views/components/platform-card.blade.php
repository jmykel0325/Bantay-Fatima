@props([
    'title',
    'icon' => 'browser',
    'badge' => null,
    'accent' => 'teal',
    'buttonLabel',
    'buttonHref',
    'buttonStyle' => 'solid',
    'note' => null,
    'features' => [],
])

@php
    $badgeClasses = match ($accent) {
        'sky' => 'bg-sky/15 text-navy',
        default => 'bg-teal/15 text-navy',
    };

    $iconWrapClasses = match ($accent) {
        'sky' => 'bg-sky/10 text-sky',
        default => 'bg-teal/10 text-teal',
    };

    $checkClasses = match ($accent) {
        'sky' => 'text-sky',
        default => 'text-teal',
    };

    $buttonClasses = match (true) {
        $buttonStyle === 'outline' => 'border border-navy/20 bg-white text-navy hover:border-navy/40 hover:bg-surface',
        $accent === 'sky' => 'bg-sky text-white hover:bg-sky/90',
        default => 'bg-teal text-white hover:bg-teal/90',
    };
@endphp

<article class="flex h-full flex-col gap-6 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 sm:p-8">
    <div class="flex flex-wrap items-center justify-between gap-3">
        <span class="inline-flex h-12 w-12 items-center justify-center rounded-xl {{ $iconWrapClasses }}">
            <x-icon :name="$icon" class="h-6 w-6" />
        </span>

        @isset($badge)
            <span class="inline-flex items-center rounded-full {{ $badgeClasses }} px-3 py-1 text-xs font-semibold">
                {{ $badge }}
            </span>
        @endisset
    </div>

    <div>
        <h3 class="text-xl font-semibold text-navy sm:text-2xl">{{ $title }}</h3>
        <p class="mt-2 text-[15px] leading-relaxed text-navy/70 sm:text-base">{{ $slot }}</p>
    </div>

    @if (count($features))
        <ul class="flex flex-1 flex-col gap-2.5">
            @foreach ($features as $feature)
                <li class="flex items-start gap-2.5 text-[15px] text-navy/80">
                    <x-icon name="check" class="mt-0.5 h-4 w-4 shrink-0 {{ $checkClasses }}" />
                    <span>{{ $feature }}</span>
                </li>
            @endforeach
        </ul>
    @endif

    <div class="mt-auto flex flex-col gap-3 pt-2">
        <a href="{{ $buttonHref }}" class="inline-flex min-h-[44px] w-full items-center justify-center rounded-xl px-6 py-3 text-center text-sm font-semibold transition-colors {{ $buttonClasses }}">
            {{ $buttonLabel }}
        </a>

        @isset($note)
            <p class="text-center text-sm text-navy/60">{{ $note }}</p>
        @endisset
    </div>
</article>
