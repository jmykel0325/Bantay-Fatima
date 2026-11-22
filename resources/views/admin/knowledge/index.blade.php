<x-admin.layout title="Knowledge Base" :breadcrumbs="['RAG', 'Approved Sources']">
    <div>
        <section>
            <div class="mb-4 flex flex-wrap items-end justify-between gap-3"><div><h2 class="text-lg font-bold text-navy">Assistant sources</h2><p class="mt-1 text-sm text-navy/55">Approved Barangay Fatima information used to answer resident questions.</p></div><div class="flex items-center gap-3"><span class="rounded-full bg-navy/5 px-3 py-1.5 text-xs font-semibold text-navy/65">{{ $documents->total() }} sources</span><a href="{{ route('admin.knowledge.create') }}" class="rounded-xl bg-teal px-4 py-2.5 text-sm font-semibold text-white">+ Add new source</a></div></div>
            <div class="space-y-4">
                @forelse($documents as $document)
                    <article class="overflow-hidden rounded-2xl border border-navy/10 bg-white shadow-sm">
                        <div class="p-5 sm:p-6">
                            <div class="flex flex-wrap items-start justify-between gap-4"><div class="min-w-0 flex-1"><div class="flex flex-wrap gap-2"><span class="rounded-full bg-teal/10 px-2.5 py-1 text-xs font-semibold text-teal">{{ ucfirst($document->source_type) }}</span><span class="rounded-full px-2.5 py-1 text-xs font-semibold {{ $document->is_approved ? 'bg-resolved/10 text-resolved' : 'bg-amber-50 text-amber-700' }}">{{ $document->is_approved ? 'Approved' : 'Not approved' }}</span></div><h3 class="mt-3 break-words text-xl font-bold text-navy">{{ $document->title }}</h3></div><div class="rounded-xl bg-navy/5 px-3 py-2 text-center"><p class="text-lg font-bold text-navy">{{ $document->chunks_count }}</p><p class="text-xs text-navy/50">indexed chunks</p></div></div>
                            <p class="mt-4 line-clamp-4 whitespace-pre-line break-words border-t border-navy/5 pt-4 text-sm leading-6 text-navy/65">{{ $document->content }}</p>
                        </div>
                        <details class="group border-t border-navy/10 bg-slate-50/70"><summary class="flex cursor-pointer list-none items-center justify-between px-5 py-3 text-sm font-semibold text-teal sm:px-6"><span>Edit source</span><span class="transition-transform group-open:rotate-180">⌄</span></summary>
                            <form method="POST" action="{{ route('admin.knowledge.update', $document) }}" class="border-t border-navy/10 p-5 sm:p-6">@csrf @method('PATCH')
                                <div class="grid gap-3 sm:grid-cols-[minmax(0,1fr)_12rem]"><label class="text-sm font-semibold text-navy">Source title<input name="title" value="{{ $document->title }}" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required></label><label class="text-sm font-semibold text-navy">Source type<select name="source_type" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">@foreach(['document','policy','faq','advisory','hotline','service_information'] as $value)<option value="{{ $value }}" @selected($document->source_type === $value)>{{ ucfirst(str_replace('_',' ',$value)) }}</option>@endforeach</select></label></div>
                                <label class="mt-4 block text-sm font-semibold text-navy">Authoritative content<textarea name="content" rows="8" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal leading-6" required>{{ $document->content }}</textarea></label>
                                <div class="mt-4 flex flex-wrap items-center justify-between gap-3"><label class="flex items-center gap-2 text-sm font-medium text-navy/70"><input type="checkbox" name="is_approved" value="1" @checked($document->is_approved)> Approved for Assistant</label><button class="rounded-xl bg-teal px-5 py-2.5 text-sm font-semibold text-white">Save and re-index</button></div>
                            </form>
                        </details>
                    </article>
                @empty
                    <div class="rounded-2xl border border-dashed border-navy/15 bg-white px-6 py-12 text-center"><p class="font-semibold text-navy">No knowledge sources yet</p><p class="mt-1 text-sm text-navy/55">Add approved barangay information for the Assistant.</p></div>
                @endforelse
            </div>
            <div class="mt-5">{{ $documents->links() }}</div>
        </section>

        @if(false)<aside class="xl:sticky xl:top-24"><form method="POST" action="{{ route('admin.knowledge.store') }}" class="overflow-hidden rounded-2xl border border-navy/10 bg-white shadow-sm">@csrf
            <div class="border-b border-navy/10 px-5 py-4"><h2 class="font-bold text-navy">Add knowledge source</h2><p class="mt-1 text-sm text-navy/55">Use verified and authoritative barangay content only.</p></div>
            <div class="space-y-4 p-5"><label class="block text-sm font-semibold text-navy">Source title<input name="title" value="{{ old('title') }}" placeholder="Descriptive source name" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal" required></label><label class="block text-sm font-semibold text-navy">Source type<select name="source_type" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal">@foreach(['document','policy','faq','advisory','hotline','service_information'] as $value)<option value="{{ $value }}" @selected(old('source_type') === $value)>{{ ucfirst(str_replace('_',' ',$value)) }}</option>@endforeach</select></label><label class="block text-sm font-semibold text-navy">Authoritative content<textarea name="content" rows="10" placeholder="Paste approved Barangay Fatima information" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal leading-6" required>{{ old('content') }}</textarea></label><label class="flex items-center gap-2 rounded-xl border border-navy/10 bg-slate-50 p-3 text-sm font-medium text-navy/75"><input type="checkbox" name="is_approved" value="1" @checked(old('is_approved'))> Approve and index now</label></div>
            <div class="border-t border-navy/10 bg-slate-50 p-5"><button class="w-full rounded-xl bg-teal p-3 font-semibold text-white">Save knowledge source</button></div>
        </form></aside>@endif
    </div>
</x-admin.layout>
