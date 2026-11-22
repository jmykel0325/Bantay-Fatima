<x-admin.layout title="Advisories" :breadcrumbs="['Content', 'Advisories']">
    <div class="grid items-start gap-6 xl:grid-cols-[minmax(0,1fr)_24rem]">
        <section aria-labelledby="advisory-list-heading">
            <div class="mb-4 flex flex-wrap items-end justify-between gap-3">
                <div>
                    <h2 id="advisory-list-heading" class="text-lg font-bold text-navy">Published content</h2>
                    <p class="mt-1 text-sm text-navy/55">Review announcements and advisories shared through Bantay Fatima.</p>
                </div>
                <span class="rounded-full bg-navy/5 px-3 py-1.5 text-xs font-semibold text-navy/65">
                    {{ $items->total() }} {{ Str::plural('item', $items->total()) }}
                </span>
            </div>

            <div class="space-y-4">
                @forelse($items as $item)
                    <article class="overflow-hidden rounded-2xl border border-navy/10 bg-white shadow-sm">
                        <div class="p-5 sm:p-6">
                            <div class="flex flex-wrap items-start justify-between gap-4">
                                <div class="min-w-0 flex-1">
                                    <div class="mb-3 flex flex-wrap items-center gap-2">
                                        <span class="rounded-full px-2.5 py-1 text-xs font-semibold {{ $item->type === 'advisory' ? 'bg-teal/10 text-teal' : 'bg-blue-50 text-blue-700' }}">
                                            {{ ucfirst($item->type) }}
                                        </span>
                                        <span class="rounded-full px-2.5 py-1 text-xs font-semibold {{ $item->status === 'published' ? 'bg-resolved/10 text-resolved' : ($item->status === 'draft' ? 'bg-amber-50 text-amber-700' : 'bg-slate-100 text-slate-600') }}">
                                            {{ ucfirst($item->status) }}
                                        </span>
                                        @if($item->is_urgent)
                                            <span class="rounded-full bg-red-50 px-2.5 py-1 text-xs font-semibold text-red-urgent">Urgent</span>
                                        @endif
                                    </div>
                                    <h3 class="break-words text-xl font-bold leading-tight text-navy">{{ $item->title }}</h3>
                                    <p class="mt-2 break-words text-sm font-medium leading-6 text-navy/70">{{ $item->summary }}</p>
                                </div>
                                <div class="shrink-0 text-right text-xs text-navy/45">
                                    <p class="font-semibold text-navy/60">{{ ucfirst($item->audience) }}</p>
                                    <p class="mt-1">{{ optional($item->updated_at)->format('M j, Y · g:i A') }}</p>
                                </div>
                            </div>

                            <div class="mt-4 border-t border-navy/5 pt-4">
                                <p class="whitespace-pre-line break-words text-sm leading-6 text-navy/70">{{ $item->content }}</p>
                            </div>
                        </div>

                        <details class="group border-t border-navy/10 bg-slate-50/70">
                            <summary class="flex cursor-pointer list-none items-center justify-between px-5 py-3 text-sm font-semibold text-teal hover:bg-slate-100 sm:px-6">
                                <span>Edit advisory</span>
                                <span class="transition-transform group-open:rotate-180" aria-hidden="true">⌄</span>
                            </summary>
                            <form method="POST" action="{{ route('admin.advisories.update', $item) }}" class="border-t border-navy/10 p-5 sm:p-6">
                                @csrf
                                @method('PATCH')
                                <div class="grid gap-4 sm:grid-cols-2">
                                    <label class="text-sm font-semibold text-navy">Title
                                        <input name="title" value="{{ $item->title }}" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required>
                                    </label>
                                    <label class="text-sm font-semibold text-navy">Content type
                                        <select name="type" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">
                                            @foreach(['announcement', 'advisory'] as $value)<option value="{{ $value }}" @selected($item->type === $value)>{{ ucfirst($value) }}</option>@endforeach
                                        </select>
                                    </label>
                                    <label class="text-sm font-semibold text-navy">Summary
                                        <input name="summary" value="{{ $item->summary }}" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal" required>
                                    </label>
                                    <label class="text-sm font-semibold text-navy">Audience
                                        <select name="audience" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">
                                            @foreach(['residents', 'staff', 'everyone'] as $value)
                                                <option value="{{ $value }}" @selected($item->audience === $value)>{{ ucfirst($value) }}</option>
                                            @endforeach
                                        </select>
                                    </label>
                                    <label class="text-sm font-semibold text-navy">Status
                                        <select name="status" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">
                                            @foreach(['draft', 'published', 'archived'] as $value)
                                                <option value="{{ $value }}" @selected($item->status === $value)>{{ ucfirst($value) }}</option>
                                            @endforeach
                                        </select>
                                    </label>
                                </div>
                                <label class="mt-4 block text-sm font-semibold text-navy">Content
                                    <textarea name="content" rows="7" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal leading-6" required>{{ $item->content }}</textarea>
                                    <span class="mt-1 block text-xs font-normal text-navy/50">Include the complete verified details, dates, locations, instructions, and contact information.</span>
                                </label>
                                <label class="mt-4 block text-sm font-semibold text-navy">Expiration date and time <span class="font-normal text-navy/45">(optional)</span>
                                    <input type="datetime-local" name="expires_at" value="{{ optional($item->expires_at)->format('Y-m-d\\TH:i') }}" class="mt-1.5 w-full rounded-xl border border-navy/15 bg-white p-3 font-normal">
                                </label>
                                <div class="mt-4 flex flex-wrap items-center justify-between gap-3">
                                    <label class="flex items-center gap-2 text-sm font-medium text-navy/75">
                                        <input type="checkbox" name="is_urgent" value="1" class="h-4 w-4 rounded border-navy/20 text-red-urgent" @checked($item->is_urgent)>
                                        Mark as urgent
                                    </label>
                                    <button class="rounded-xl bg-teal px-5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-teal/90">Save changes</button>
                                </div>
                            </form>
                        </details>
                    </article>
                @empty
                    <div class="rounded-2xl border border-dashed border-navy/15 bg-white px-6 py-12 text-center">
                        <p class="font-semibold text-navy">No advisories yet</p>
                        <p class="mt-1 text-sm text-navy/55">Use the publishing form to create the first advisory.</p>
                    </div>
                @endforelse
            </div>

            <div class="mt-5">{{ $items->links() }}</div>
        </section>

        <aside class="xl:sticky xl:top-24">
            <form method="POST" action="{{ route('admin.advisories.store') }}" class="overflow-hidden rounded-2xl border border-navy/10 bg-white shadow-sm">
                @csrf
                <div class="border-b border-navy/10 px-5 py-4">
                    <h2 class="font-bold text-navy">Create advisory</h2>
                    <p class="mt-1 text-sm text-navy/55">Publish a clear update for the selected audience.</p>
                </div>
                <div class="space-y-4 p-5">
                    <label class="block text-sm font-semibold text-navy">Title
                        <input name="title" value="{{ old('title') }}" placeholder="Short, specific headline" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal" required>
                    </label>
                    <label class="block text-sm font-semibold text-navy">Summary
                        <textarea name="summary" rows="2" placeholder="Brief overview for residents" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal leading-5" required>{{ old('summary') }}</textarea>
                        <span class="mt-1 block text-xs font-normal text-navy/50">A short notification preview. Keep it clear and under 500 characters.</span>
                    </label>
                    <label class="block text-sm font-semibold text-navy">Full content
                        <textarea name="content" rows="8" placeholder="Include dates, times, location, instructions, affected residents, and official contact details." class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal leading-6" required>{{ old('content') }}</textarea>
                        <span class="mt-1 block text-xs font-normal text-navy/50">Use verified information only. This is the full text residents will read.</span>
                    </label>

                    <div class="grid gap-3 sm:grid-cols-2 xl:grid-cols-1 2xl:grid-cols-2">
                        <label class="text-sm font-semibold text-navy">Content type
                            <select name="type" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal">
                                <option value="announcement" @selected(old('type') === 'announcement')>Announcement</option>
                                <option value="advisory" @selected(old('type') === 'advisory')>Advisory</option>
                            </select>
                        </label>
                        <label class="text-sm font-semibold text-navy">Audience
                            <select name="audience" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal">
                                @foreach(['everyone', 'residents', 'staff'] as $value)
                                    <option value="{{ $value }}" @selected(old('audience', 'everyone') === $value)>{{ ucfirst($value) }}</option>
                                @endforeach
                            </select>
                        </label>
                        <label class="text-sm font-semibold text-navy">Publishing status
                            <select name="status" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal">
                                <option value="published" @selected(old('status', 'published') === 'published')>Published</option>
                                <option value="draft" @selected(old('status') === 'draft')>Draft</option>
                            </select>
                        </label>
                        <label class="flex items-end">
                            <span class="flex min-h-12 w-full items-center gap-2 rounded-xl border border-navy/10 bg-slate-50 px-3 text-sm font-medium text-navy/75">
                                <input type="checkbox" name="is_urgent" value="1" class="h-4 w-4 rounded border-navy/20 text-red-urgent" @checked(old('is_urgent'))>
                                Urgent advisory
                            </span>
                        </label>
                        <label class="text-sm font-semibold text-navy sm:col-span-2 xl:col-span-1 2xl:col-span-2">Expiration date and time <span class="font-normal text-navy/45">(optional)</span>
                            <input type="datetime-local" name="expires_at" value="{{ old('expires_at') }}" class="mt-1.5 w-full rounded-xl border border-navy/15 p-3 font-normal">
                            <span class="mt-1 block text-xs font-normal text-navy/50">The item stops appearing to residents after this time.</span>
                        </label>
                    </div>
                </div>
                <div class="border-t border-navy/10 bg-slate-50 p-5">
                    <button class="w-full rounded-xl bg-teal px-4 py-3 font-semibold text-white shadow-sm hover:bg-teal/90">Publish advisory</button>
                </div>
            </form>
        </aside>
    </div>
</x-admin.layout>
