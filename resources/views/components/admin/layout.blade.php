@props(['title','breadcrumbs'=>[]])
<x-layouts.app :title="$title.' — Bantay Fatima'">
<div class="min-h-screen bg-surface" data-admin-shell>
    <div id="admin-overlay" class="fixed inset-0 z-30 hidden bg-navy/50 lg:hidden"></div>
    <aside id="admin-sidebar" class="fixed inset-y-0 left-0 z-40 w-72 -translate-x-full bg-navy p-5 text-white shadow-xl transition-transform lg:translate-x-0">
        <div class="flex items-center justify-between"><x-brand-lockup variant="light" /><button id="close-sidebar" class="rounded-lg p-2 text-white hover:bg-white/10 lg:hidden" aria-label="Close menu"><x-icon name="close" class="h-5 w-5" /></button></div>
        <nav class="mt-8 space-y-1" aria-label="Administrator navigation">
            <x-admin.sidebar-link :href="route('admin.dashboard')" label="Dashboard" icon="dashboard" :active="request()->routeIs('admin.dashboard')" />
            <x-admin.sidebar-link :href="route('admin.reports.index')" label="Community Reports" icon="reports" :active="request()->routeIs('admin.reports.*')" />
            <x-admin.sidebar-link label="Report Map" icon="pin" disabled />
            <x-admin.sidebar-link :href="route('admin.users.index')" label="User Management" icon="team" :active="request()->routeIs('admin.users.*')" />
            @foreach(['Advisories','Emergency Information','Knowledge Base','Notifications','Analytics','Audit Logs','System Settings','Backup and Restore','My Profile'] as $item)
                <x-admin.sidebar-link :label="$item" :icon="match($item) { 'Advisories' => 'megaphone', 'Emergency Information' => 'phone', 'Knowledge Base' => 'book', 'Notifications' => 'bell', 'Analytics' => 'chart', 'System Settings' => 'settings', 'Backup and Restore' => 'database', 'My Profile' => 'user', default => 'reports' }" disabled />
            @endforeach
        </nav>
        <form method="POST" action="{{ route('admin.logout') }}" class="mt-6">@csrf<button class="w-full rounded-xl border border-white/15 px-3 py-2.5 text-left text-sm font-semibold hover:bg-white/10">Logout</button></form>
    </aside>
    <div class="lg:pl-72">
        <header class="sticky top-0 z-20 border-b border-navy/5 bg-white/95 backdrop-blur">
            <div class="flex min-h-18 items-center justify-between gap-4 px-4 sm:px-6 lg:px-8">
                <button id="open-sidebar" class="rounded-xl border border-navy/10 p-2.5 text-navy hover:bg-slate-50 lg:hidden" aria-label="Open menu"><x-icon name="menu" class="h-5 w-5" /></button>
                <div class="min-w-0"><p class="truncate text-sm text-navy/50">{{ implode(' / ', $breadcrumbs) }}</p><h1 class="truncate text-lg font-bold text-navy">{{ $title }}</h1></div>
                <div class="flex items-center gap-3"><span class="relative rounded-xl bg-teal/10 p-2 text-teal" aria-label="Notifications"><x-icon name="bell" class="h-5 w-5" /><span class="absolute right-1 top-1 h-2 w-2 rounded-full bg-red-urgent ring-2 ring-white"></span></span><div class="hidden text-right sm:block"><p class="text-sm font-semibold text-navy">{{ auth()->user()->first_name }} {{ auth()->user()->last_name }}</p><p class="text-xs text-navy/50">Administrator</p></div></div>
            </div>
        </header>
        <main id="main-content" class="p-4 sm:p-6 lg:p-8">
            @if(session('success'))<div class="mb-5 rounded-xl border border-emerald-200 bg-emerald-50 p-4 text-sm text-emerald-800" role="status">{{ session('success') }}</div>@endif
            @if($errors->any())<div class="mb-5 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700" role="alert">{{ $errors->first() }}</div>@endif
            @if(request()->routeIs('admin.reports.show') && request()->route('report'))
                <x-report-photo-gallery :report="request()->route('report')" class="mb-6" />
            @endif
            {{ $slot }}
        </main>
    </div>
</div>
<script>document.addEventListener('DOMContentLoaded',()=>{const s=document.getElementById('admin-sidebar'),o=document.getElementById('admin-overlay');const toggle=v=>{s.classList.toggle('-translate-x-full',!v);o.classList.toggle('hidden',!v)};document.getElementById('open-sidebar')?.addEventListener('click',()=>toggle(true));document.getElementById('close-sidebar')?.addEventListener('click',()=>toggle(false));o?.addEventListener('click',()=>toggle(false));});</script>
</x-layouts.app>
