@props([
    'theme' => 'light', // light | dark
    'size' => 'sm', // sm | lg
    'stack' => false, // false = compact single row (navbar) | 'mobile' = stack on mobile, row on sm+ (footer) | 'always' = always stacked, centered (login)
    'showSeal' => ! auth()->check(),
])

@php
    // Match the wordmark height to the seal so both sit at the same size.
    $logoSize = auth()->check() ? 'lg' : ($size === 'lg' ? 'xl' : 'nav');

    $sealClasses = $size === 'lg' ? 'h-20 w-20' : 'h-11 w-11';
    $titleClasses = $size === 'lg' ? 'text-2xl sm:text-3xl' : 'text-base sm:text-lg';
    $subtitleClasses = $size === 'lg' ? 'text-xs sm:text-sm' : 'text-[10px] sm:text-xs';

    $titleColor = $theme === 'dark' ? 'text-white' : 'text-navy';
    $subtitleColor = $theme === 'dark' ? 'text-white/70' : 'text-navy/70';

    $rowClasses = match ($stack) {
        'always' => 'flex flex-col items-center gap-3 text-center',
        'mobile' => 'flex flex-col items-center gap-3 text-center sm:flex-row sm:items-center sm:text-left',
        default => 'flex items-center gap-2 sm:gap-3',
    };

    $subtitleAlignClasses = match ($stack) {
        'always' => 'mx-auto',
        'mobile' => 'mx-auto sm:mx-0',
        default => '',
    };
@endphp

<div {{ $attributes->merge(['class' => $rowClasses]) }}>
    <div class="flex shrink-0 items-center gap-2 sm:gap-3">
        @if($showSeal)
            <img
                src="{{ asset('assets/FatimaLogo.png') }}"
                alt="Official seal of Barangay Fatima, General Santos City"
                class="{{ $sealClasses }} shrink-0 object-contain"
            >
        @endif
        <x-logo :size="$logoSize" />
    </div>
</div>
