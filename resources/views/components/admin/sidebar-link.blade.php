@props(['href' => '#', 'active' => false, 'label', 'icon' => 'reports', 'disabled' => false])
<a href="{{ $disabled ? '#' : $href }}" @if($disabled) aria-disabled="true" @endif
   class="flex items-center justify-between rounded-xl px-3 py-2.5 text-sm font-medium transition {{ $active ? 'bg-teal text-white shadow-sm' : 'text-white/75 hover:bg-white/10 hover:text-white' }} {{ $disabled ? 'cursor-not-allowed opacity-55' : '' }}">
    <span class="flex items-center gap-3"><x-icon :name="$icon" class="h-5 w-5" />{{ $label }}</span>
    @if($disabled)<span class="rounded bg-white/10 px-1.5 py-0.5 text-[10px]">Soon</span>@endif
</a>
