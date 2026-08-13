@props(['value'])
@php($class = match($value) {'resolved','active','low','validated' => 'bg-resolved/10 text-resolved', 'in_progress','normal' => 'bg-primary/10 text-primary', 'urgent','suspended','rejected' => 'bg-red-urgent/10 text-red-urgent', 'high','pending','unverified' => 'bg-amber/10 text-amber', default => 'bg-slate-100 text-slate-700'})
<span {{ $attributes->class("inline-flex rounded-full px-2.5 py-1 text-xs font-semibold $class") }}>{{ str($value)->replace('_',' ')->title() }}</span>
