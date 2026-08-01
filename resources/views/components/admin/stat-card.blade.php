@props(['label','value','href'=>'#','tone'=>'teal','icon'=>'chart','clickable'=>true])
@php
    $iconWrapClasses = match ($tone) {
        'sky' => 'bg-sky/10 text-sky',
        'amber' => 'bg-amber/15 text-amber',
        'red' => 'bg-red-urgent/10 text-red-urgent',
        'navy' => 'bg-navy/10 text-navy',
        default => 'bg-teal/10 text-teal',
    };

    $linkColorClasses = match ($tone) {
        'sky' => 'text-sky',
        'amber' => 'text-amber',
        'red' => 'text-red-urgent',
        'navy' => 'text-navy',
        default => 'text-teal',
    };

    $tag = $clickable ? 'a' : 'div';
@endphp
<{{ $tag }} @if($clickable) href="{{ $href }}" @endif class="group flex flex-col rounded-2xl bg-white p-5 shadow-sm ring-1 ring-navy/5 transition {{ $clickable ? 'hover:-translate-y-0.5 hover:shadow-md' : '' }}">
    <span class="inline-flex h-10 w-10 shrink-0 items-center justify-center rounded-xl {{ $iconWrapClasses }}"><x-icon :name="$icon" class="h-5 w-5" /></span>
    <p class="mt-4 text-sm font-medium text-navy/60">{{ $label }}</p>
    <p class="mt-1 text-2xl font-bold text-navy">{{ $value }}</p>
    @if($clickable)
        <p class="mt-3 text-xs font-semibold {{ $linkColorClasses }} group-hover:underline">View details →</p>
    @else
        <p class="mt-3 text-xs font-medium text-navy/40">Live metric</p>
    @endif
</{{ $tag }}>
