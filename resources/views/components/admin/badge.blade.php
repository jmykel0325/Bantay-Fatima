@props(['value'])
@php($class = match($value) {'resolved','active','low','validated' => 'bg-emerald-50 text-emerald-700', 'in_progress','normal' => 'bg-blue-50 text-blue-700', 'urgent','suspended','rejected' => 'bg-red-50 text-red-700', 'high','pending','unverified' => 'bg-amber-50 text-amber-700', default => 'bg-slate-100 text-slate-700'})
<span {{ $attributes->class("inline-flex rounded-full px-2.5 py-1 text-xs font-semibold $class") }}>{{ str($value)->replace('_',' ')->title() }}</span>
