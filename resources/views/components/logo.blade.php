@props(['size' => 'md'])

@php
    $sizeClasses = match ($size) {
        'sm' => 'h-8 w-8',
        'lg' => 'h-14 w-14',
        'xl' => 'h-20 w-20',
        default => 'h-10 w-10',
    };

@endphp

<img
    src="{{ asset('assets/BantayFatimaLogo.png') }}"
    alt="Bantay Fatima logo"
    {{ $attributes->merge(['class' => "shrink-0 object-contain $sizeClasses"]) }}
>
