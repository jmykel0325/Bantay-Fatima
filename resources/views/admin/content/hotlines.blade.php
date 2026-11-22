<x-admin.layout title="Emergency Information" :breadcrumbs="['Content', 'Hotlines']">
    <div class="grid items-start gap-6 xl:grid-cols-[minmax(0,1fr)_24rem]">
        <section>
            <div class="mb-4 flex items-end justify-between gap-3">
                <div><h2 class="text-lg font-bold text-navy">Emergency directory</h2><p class="mt-1 text-sm text-navy/55">Official contact numbers available to residents.</p></div>
                <span class="rounded-full bg-navy/5 px-3 py-1.5 text-xs font-semibold text-navy/65">{{ $items->total() }} contacts</span>
            </div>
            <div class="grid gap-4 lg:grid-cols-2">
                @forelse($items as $item)
                    <article class="overflow-hidden rounded-2xl border border-navy/10 bg-white shadow-sm">
                        <div class="p-5">
                            <div class="flex items-start justify-between gap-3">
                                <div class="min-w-0"><span class="text-xs font-bold uppercase tracking-wide text-teal">{{ $item->group }}</span><h3 class="mt-1 break-words text-lg font-bold text-navy">{{ $item->organization }}</h3></div>
                                <span class="rounded-full px-2.5 py-1 text-xs font-semibold {{ $item->is_active ? 'bg-resolved/10 text-resolved' : 'bg-slate-100 text-slate-500' }}">{{ $item->is_active ? 'Active' : 'Inactive' }}</span>
                            </div>
                            <div class="mt-4 rounded-xl bg-navy/5 p-4"><p class="text-xs font-semibold uppercase tracking-wide text-navy/45">Primary hotline</p><p class="mt-1 break-all text-xl font-bold text-navy">{{ $item->hotline }}</p>@if($item->alternative_number)<p class="mt-1 text-sm text-navy/60">Alternative: {{ $item->alternative_number }}</p>@endif</div>
                            <dl class="mt-4 space-y-2 text-sm">@if($item->hours)<div class="flex gap-2"><dt class="w-16 shrink-0 font-semibold text-navy">Hours</dt><dd class="text-navy/65">{{ $item->hours }}</dd></div>@endif @if($item->address)<div class="flex gap-2"><dt class="w-16 shrink-0 font-semibold text-navy">Address</dt><dd class="text-navy/65">{{ $item->address }}</dd></div>@endif</dl>
                            @if($item->description)<p class="mt-4 border-t border-navy/5 pt-4 text-sm leading-6 text-navy/65">{{ $item->description }}</p>@endif
                        </div>
                        <details class="group border-t border-navy/10 bg-slate-50/70">
                            <summary class="flex cursor-pointer list-none items-center justify-between px-5 py-3 text-sm font-semibold text-teal"><span>Edit contact</span><span class="transition-transform group-open:rotate-180">⌄</span></summary>
                            <form method="POST" action="{{ route('admin.hotlines.update', $item) }}" class="grid gap-3 border-t border-navy/10 p-5 sm:grid-cols-2">@csrf @method('PATCH')
                                @foreach(['organization'=>'Organization','hotline'=>'Primary hotline','alternative_number'=>'Alternative number','hours'=>'Operating hours','address'=>'Address'] as $name=>$label)
                                    <label class="text-sm font-semibold text-navy {{ $name === 'address' ? 'sm:col-span-2' : '' }}">{{ $label }}<input name="{{ $name }}" value="{{ $item->$name }}" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" @required(in_array($name,['group','organization','hotline']))></label>
                                @endforeach
                                <label class="text-sm font-semibold text-navy">Service group<select name="group" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required>
                                    @foreach(['medical'=>'Medical','police'=>'Police','pnp'=>'Police / PNP (legacy)','fire'=>'Fire service','ambulance'=>'Ambulance','rescue'=>'Rescue','barangay'=>'Barangay','disaster'=>'Disaster response','other'=>'Other'] as $value=>$label)<option value="{{ $value }}" @selected(strtolower($item->group) === $value)>{{ $label }}</option>@endforeach
                                </select></label>
                                <label class="text-sm font-semibold text-navy">Emergency icon<select name="icon_type" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">
                                    @foreach(['medical'=>'Medical / Hospital','police'=>'Police / PNP','pnp'=>'Police / PNP','fire'=>'Fire','ambulance'=>'Ambulance','rescue'=>'Rescue','barangay'=>'Barangay','disaster'=>'Disaster response','other'=>'Other'] as $value=>$label)<option value="{{ $value }}" @selected(($item->icon_type ?: 'other') === $value)>{{ $label }}</option>@endforeach
                                </select></label>
                                <label class="text-sm font-semibold text-navy">Contact person / officer on duty<input name="contact_person" value="{{ $item->contact_person }}" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal"></label>
                                <label class="text-sm font-semibold text-navy">Availability type<select name="availability_type" data-hours-type class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal"><option value="24_7" @selected(($item->availability_type ?: '24_7') === '24_7')>24/7</option><option value="custom" @selected($item->availability_type === 'custom')>Custom hours</option></select></label>
                                <label class="text-sm font-semibold text-navy">Coverage area<input name="coverage_area" value="{{ $item->coverage_area }}" required class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal"></label>
                                <label class="text-sm font-semibold text-navy">Status<select name="status" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal"><option value="active" @selected(($item->status ?: ($item->is_active ? 'active' : 'inactive')) === 'active')>Active</option><option value="temporarily_unavailable" @selected($item->status === 'temporarily_unavailable')>Temporarily unavailable</option><option value="inactive" @selected(($item->status ?: 'inactive') === 'inactive')>Inactive</option></select></label>
                                <label class="text-sm font-semibold text-navy">Priority<select name="priority" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">@foreach(['high','normal','low'] as $value)<option value="{{ $value }}" @selected(($item->priority ?: 'normal') === $value)>{{ ucfirst($value) }}</option>@endforeach</select></label>
                                <label class="text-sm font-semibold text-navy sm:col-span-2">Description<textarea name="description" rows="3" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">{{ $item->description }}</textarea></label>
                                <input type="hidden" name="sort_order" value="{{ $item->sort_order }}">
                                <div class="flex items-center justify-between gap-3 sm:col-span-2"><label class="flex items-center gap-2 text-sm font-medium text-navy/70"><input type="checkbox" name="is_active" value="1" @checked($item->is_active)> Active</label><button class="rounded-xl bg-teal px-5 py-2.5 text-sm font-semibold text-white">Save changes</button></div>
                            </form>
                        </details>
                    </article>
                @empty
                    <div class="rounded-2xl border border-dashed border-navy/15 bg-white px-6 py-12 text-center lg:col-span-2"><p class="font-semibold text-navy">No emergency contacts yet</p><p class="mt-1 text-sm text-navy/55">Add the first verified hotline using the form.</p></div>
                @endforelse
            </div>
            <div class="mt-5">{{ $items->links() }}</div>
        </section>

        <aside class="xl:sticky xl:top-24">
            <a href="{{ route('admin.hotlines.create') }}" class="block rounded-2xl border border-navy/10 bg-white p-5 text-center shadow-sm">
                <span class="block rounded-xl bg-teal px-4 py-3 font-semibold text-white">＋ Add emergency contact</span>
                <span class="mt-3 block text-sm text-navy/55">Create a structured emergency contact for residents.</span>
            </a>
        </aside>
        {{--
            <form method="POST" action="{{ route('admin.hotlines.store') }}" class="overflow-hidden rounded-2xl border border-navy/10 bg-white shadow-sm">@csrf
                <div class="border-b border-navy/10 px-5 py-4"><h2 class="font-bold text-navy">Add emergency contact</h2><p class="mt-1 text-sm text-navy/55">Enter a verified public contact number.</p></div>
                <div class="space-y-4 p-5">
                    <label class="block text-sm font-semibold text-navy">Service group <span class="text-red-urgent">*</span><select name="group" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required>
                        @foreach(['medical'=>'Medical','police'=>'Police / PNP','fire'=>'Fire service','ambulance'=>'Ambulance','rescue'=>'Rescue','barangay'=>'Barangay','disaster'=>'Disaster response','other'=>'Other'] as $value=>$label)<option value="{{ $value }}" @selected(old('group', 'other') === $value)>{{ $label }}</option>@endforeach
                    </select><span class="mt-1 block text-xs font-normal text-navy/55">Choose the service category for this contact.</span></label>
                    @foreach(['organization'=>'Organization','hotline'=>'Primary hotline','alternative_number'=>'Alternative number','hours'=>'Operating hours','address'=>'Office address'] as $name=>$label)
                        <label class="block text-sm font-semibold text-navy">{{ $label }} @if(in_array($name,['group','organization','hotline']))<span class="text-red-urgent">*</span>@endif<input name="{{ $name }}" value="{{ old($name) }}" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal" @required(in_array($name,['group','organization','hotline']))></label>
                    @endforeach
                    <label class="block text-sm font-semibold text-navy">Emergency icon<select name="icon_type" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">
                        @foreach(['medical'=>'Medical / Hospital','police'=>'Police / PNP','fire'=>'Fire','ambulance'=>'Ambulance','rescue'=>'Rescue','barangay'=>'Barangay','disaster'=>'Disaster response','other'=>'Other'] as $value=>$label)<option value="{{ $value }}" @selected(old('icon_type', 'other') === $value)>{{ $label }}</option>@endforeach
                    </select><span class="mt-1 block text-xs font-normal text-navy/55">This icon is shown beside the contact in the resident app.</span></label>
                    <label class="block text-sm font-semibold text-navy">Contact person / officer on duty<input name="contact_person" value="{{ old('contact_person') }}" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal"></label>
                    <label class="block text-sm font-semibold text-navy">Availability type <span class="text-red-urgent">*</span><select name="availability_type" data-hours-type class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required><option value="24_7" @selected(old('availability_type','24_7') === '24_7')>24/7</option><option value="custom" @selected(old('availability_type') === 'custom')>Custom hours</option></select></label>
                    <label class="block text-sm font-semibold text-navy">Coverage area <span class="text-red-urgent">*</span><input name="coverage_area" value="{{ old('coverage_area') }}" placeholder="Barangay Fatima / General Santos City" required class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal"><span class="mt-1 block text-xs font-normal text-navy/55">Area where this emergency service can respond.</span></label>
                    <label class="block text-sm font-semibold text-navy">Status <span class="text-red-urgent">*</span><select name="status" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required><option value="active">Active</option><option value="temporarily_unavailable">Temporarily unavailable</option><option value="inactive">Inactive</option></select></label>
                    <label class="block text-sm font-semibold text-navy">Priority<select name="priority" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">@foreach(['high','normal','low'] as $value)<option value="{{ $value }}" @selected(old('priority','normal') === $value)>{{ ucfirst($value) }}</option>@endforeach</select></label>
                    <label class="block text-sm font-semibold text-navy">Last verified date<input type="date" name="last_verified_at" value="{{ old('last_verified_at') }}" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal"></label>
                    <label class="flex items-center gap-2 text-sm font-medium text-navy/75"><input type="checkbox" name="show_in_resident_app" value="1" checked> Show in resident app</label>
                    <label class="block text-sm font-semibold text-navy">Description / When to contact <span class="text-red-urgent">*</span><textarea name="description" rows="3" required class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal leading-6">{{ old('description') }}</textarea></label>
                    <input type="hidden" name="is_active" value="1">
                </div>
                <div class="border-t border-navy/10 bg-slate-50 p-5"><button class="w-full rounded-xl bg-teal p-3 font-semibold text-white">Add hotline</button></div>
            </form>
        </aside> --}}
    </div>
</x-admin.layout>
<script>
document.querySelectorAll('[data-hours-type]').forEach(function(select){
    function sync(){const form=select.closest('form');const input=form.querySelector('[name="hours"]');if(!input)return;const custom=select.value==='custom';input.disabled=!custom;input.required=custom;if(!custom)input.value='24/7';}
    select.addEventListener('change',sync);sync();
});
</script>
