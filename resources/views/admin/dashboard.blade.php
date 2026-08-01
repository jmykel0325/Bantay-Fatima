<x-admin.layout title="Dashboard" :breadcrumbs="['Administration','Dashboard']">

    {{-- ================= HEADER ================= --}}
    <section class="flex flex-col gap-4 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 sm:flex-row sm:items-center sm:justify-between">
        <div>
            <p class="text-sm font-semibold text-teal">{{ now()->format('l, F j, Y') }}</p>
            <h2 class="mt-1 text-2xl font-bold text-navy">Welcome back, {{ auth()->user()->first_name }}</h2>
            <p class="mt-1 text-navy/60">Here is the latest activity and status of Bantay Fatima.</p>
        </div>
        <a href="{{ route('admin.reports.export') }}" class="inline-flex min-h-[44px] items-center justify-center gap-2 rounded-xl border border-navy/15 bg-white px-5 text-sm font-semibold text-navy transition-colors hover:border-navy/30 hover:bg-surface">
            <x-icon name="download" class="h-4 w-4" />
            Export Reports
        </a>
    </section>

    {{-- ================= REPORT PIPELINE ================= --}}
    <section class="mt-6">
        <h3 class="text-xs font-semibold uppercase tracking-wide text-navy/50">Report Pipeline</h3>
        <div class="mt-3 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
            <x-admin.stat-card
                label="Total Reports" :value="$stats['total']" icon="reports" tone="navy"
                :href="route('admin.reports.index')"
            />
            <x-admin.stat-card
                label="Pending Reports" :value="$stats['pending']" icon="clock" tone="amber"
                :href="route('admin.reports.index', ['status' => 'pending'])"
            />
            <x-admin.stat-card
                label="In Progress Reports" :value="$stats['in_progress']" icon="timeline" tone="sky"
                :href="route('admin.reports.index', ['status' => 'in_progress'])"
            />
            <x-admin.stat-card
                label="Resolved Reports" :value="$stats['resolved']" icon="check" tone="teal"
                :href="route('admin.reports.index', ['status' => 'resolved'])"
            />
        </div>
    </section>

    {{-- ================= OPERATIONS ================= --}}
    <section class="mt-6">
        <h3 class="text-xs font-semibold uppercase tracking-wide text-navy/50">Operations</h3>
        <div class="mt-3 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
            <x-admin.stat-card
                label="Total Residents" :value="$stats['residents']" icon="team" tone="navy"
                :href="route('admin.users.index', ['role' => 'resident'])"
            />
            <x-admin.stat-card
                label="Active Staff" :value="$stats['staff']" icon="user" tone="teal"
                :href="route('admin.users.index', ['role' => 'staff', 'status' => 'active'])"
            />
            <x-admin.stat-card
                label="Unread Notifications" :value="$stats['notifications']" icon="bell" tone="red"
                :clickable="false"
            />
            <x-admin.stat-card
                label="Average Resolution Time" :value="$stats['average_resolution']" icon="chart" tone="sky"
                :clickable="false"
            />
        </div>
    </section>

    {{-- ================= CHARTS ================= --}}
    <section class="mt-6 grid gap-6 xl:grid-cols-2">
        <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
            <div class="flex items-center gap-3">
                <span class="inline-flex h-9 w-9 items-center justify-center rounded-xl bg-teal/10 text-teal"><x-icon name="chart" class="h-4.5 w-4.5" /></span>
                <h3 class="font-bold text-navy">Reports Submitted per Month</h3>
            </div>
            <div class="mt-6 space-y-3">
                @forelse ($monthly as $month => $count)
                    <div>
                        <div class="flex justify-between text-xs text-navy/60">
                            <span>{{ \Carbon\Carbon::createFromFormat('Y-m', $month)->format('M Y') }}</span>
                            <span class="font-semibold text-navy">{{ $count }}</span>
                        </div>
                        <div class="mt-1 h-2 rounded-full bg-navy/5">
                            <div class="h-2 rounded-full bg-teal" style="width: {{ max(4, ($count / max(1, $monthly->max())) * 100) }}%"></div>
                        </div>
                    </div>
                @empty
                    <x-admin.empty-state />
                @endforelse
            </div>
        </div>

        <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
            <div class="flex items-center gap-3">
                <span class="inline-flex h-9 w-9 items-center justify-center rounded-xl bg-sky/10 text-sky"><x-icon name="reports" class="h-4.5 w-4.5" /></span>
                <h3 class="font-bold text-navy">Reports by Category</h3>
            </div>
            <div class="mt-6 space-y-3">
                @php $categoryTones = ['bg-teal', 'bg-sky', 'bg-amber', 'bg-navy']; @endphp
                @forelse ($byCategory as $label => $count)
                    <div>
                        <div class="flex justify-between text-xs text-navy/60">
                            <span>{{ $label }}</span>
                            <span class="font-semibold text-navy">{{ $count }}</span>
                        </div>
                        <div class="mt-1 h-2 rounded-full bg-navy/5">
                            <div class="h-2 rounded-full {{ $categoryTones[$loop->index % 4] }}" style="width: {{ max(4, ($count / max(1, $byCategory->max())) * 100) }}%"></div>
                        </div>
                    </div>
                @empty
                    <x-admin.empty-state />
                @endforelse
            </div>
        </div>
    </section>

    {{-- ================= RECENT REPORTS + ACTIVITY ================= --}}
    <section class="mt-6 grid gap-6 xl:grid-cols-3">
        <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 xl:col-span-2">
            <div class="flex items-center justify-between">
                <h3 class="font-bold text-navy">Recent Reports</h3>
                <a href="{{ route('admin.reports.index') }}" class="text-sm font-semibold text-teal hover:underline">View all</a>
            </div>
            <div class="mt-4 overflow-x-auto">
                <table class="w-full min-w-[820px] text-left text-sm">
                    <thead class="border-b text-xs uppercase text-navy/45">
                        <tr>
                            <th class="py-3 pr-3">Reference</th>
                            <th class="pr-3">Title</th>
                            <th class="pr-3">Reporter</th>
                            <th class="pr-3">Category</th>
                            <th class="pr-3">Priority</th>
                            <th class="pr-3">Status</th>
                            <th class="pr-3">Submitted</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-navy/5">
                        @forelse ($recentReports as $report)
                            <tr class="transition-colors hover:bg-surface">
                                <td class="py-3 pr-3 font-semibold text-navy">{{ $report->reference_number }}</td>
                                <td class="pr-3 text-navy/80">{{ $report->title }}</td>
                                <td class="pr-3 text-navy/80">{{ $report->resident->first_name }} {{ $report->resident->last_name }}</td>
                                <td class="pr-3 text-navy/60">{{ $report->category->name }}</td>
                                <td class="pr-3"><x-admin.badge :value="$report->priority" /></td>
                                <td class="pr-3"><x-admin.badge :value="$report->status" /></td>
                                <td class="pr-3 whitespace-nowrap text-navy/50">{{ $report->created_at->diffForHumans() }}</td>
                                <td><a class="font-semibold text-teal hover:underline" href="{{ route('admin.reports.show', $report) }}">View</a></td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="8" class="py-10 text-center text-navy/50">No reports have been submitted yet.</td>
                            </tr>
                        @endforelse
                    </tbody>
                </table>
            </div>
        </div>

        <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
            <h3 class="font-bold text-navy">Recent Activity</h3>
            <ul class="mt-4 space-y-4">
                @forelse ($activities as $activity)
                    @php
                        $group = explode('.', $activity->action)[0] ?? 'system';
                        [$activityIcon, $activityTone] = match ($group) {
                            'report' => ['reports', 'bg-teal/10 text-teal'],
                            'staff', 'user' => ['team', 'bg-sky/10 text-sky'],
                            default => ['settings', 'bg-navy/10 text-navy'],
                        };
                    @endphp
                    <li class="flex gap-3">
                        <span class="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg {{ $activityTone }}">
                            <x-icon :name="$activityIcon" class="h-4 w-4" />
                        </span>
                        <div class="min-w-0">
                            <p class="text-sm text-navy/80">{{ $activity->description }}</p>
                            <p class="mt-0.5 text-xs text-navy/45">
                                {{ $activity->user ? $activity->user->first_name.' '.$activity->user->last_name : 'System' }}
                                &middot; {{ $activity->created_at->diffForHumans() }}
                            </p>
                        </div>
                    </li>
                @empty
                    <x-admin.empty-state />
                @endforelse
            </ul>
        </div>
    </section>
</x-admin.layout>
