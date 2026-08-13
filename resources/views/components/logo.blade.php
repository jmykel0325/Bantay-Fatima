@props(['size' => 'md'])

@php
    $sizeClasses = match ($size) {
        'sm' => 'h-8 w-auto',
        // `nav`/`xl` are optically matched to the seal in <x-brand-lockup>: the
        // wordmark is a full-bleed rounded square, so at an identical box height
        // it reads larger than the circular seal. ~90% evens them out.
        'nav' => 'h-10 w-auto',
        'lg' => 'h-14 w-auto',
        'xl' => 'h-18 w-auto',
        default => 'h-10 w-auto',
    };

@endphp

<img
    src="{{ asset('assets/BantayFatimaLogo.png') }}"
    alt="Bantay Fatima logo"
    {{ $attributes->merge(['class' => "shrink-0 object-contain $sizeClasses"]) }}
>
