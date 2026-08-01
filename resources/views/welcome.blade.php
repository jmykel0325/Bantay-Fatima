<x-layouts.app
    title="Bantay Fatima — Community Platform for Barangay Fatima"
    description="Bantay Fatima is a geo-tagged community issue reporting, status tracking, and barangay response management platform for Barangay Fatima, accessible on the web and through the Android application."
>
    <x-navbar />

    <main id="main-content">

        {{-- ================= HERO ================= --}}
        <section id="home" class="overflow-hidden bg-white py-16 sm:py-20 lg:py-28">
            <div class="wrap grid items-center gap-12 lg:grid-cols-2 lg:gap-16">
                <div>
                    <span class="inline-flex items-center rounded-full bg-teal/10 px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-teal">
                        Community Platform
                    </span>

                    <h1 class="mt-5 text-4xl font-bold leading-tight text-navy sm:text-5xl lg:text-[3.25rem]">
                        A Safer and More Connected Barangay Fatima
                    </h1>

                    <p class="mt-5 max-w-xl text-base leading-relaxed text-navy/70 sm:text-lg">
                        Report community concerns, receive important barangay updates, and follow the progress of submitted reports through the Bantay Fatima Android application.
                    </p>

                    <div class="mt-8 flex flex-col gap-3 sm:flex-row">
                        <a href="#how-it-works" class="inline-flex min-h-[44px] w-full items-center justify-center rounded-xl bg-teal px-7 text-sm font-semibold text-white transition-colors hover:bg-teal/90 sm:w-auto">
                            Learn How It Works
                        </a>
                        <a href="#android-app" class="inline-flex min-h-[44px] w-full items-center justify-center rounded-xl border border-navy/15 bg-white px-7 text-sm font-semibold text-navy transition-colors hover:border-navy/30 hover:bg-surface sm:w-auto">
                            Android App Coming Soon
                        </a>
                    </div>

                    <p class="mt-6 max-w-md text-sm leading-relaxed text-navy/50">
                        Bantay Fatima connects community reporting with organized barangay review and response.
                    </p>

                    <p class="mt-3 flex items-start gap-2 text-sm font-medium leading-relaxed text-navy/80">
                        <x-icon name="sync" class="mt-0.5 h-4 w-4 shrink-0 text-teal" />
                        Residents and authorized staff will access Bantay Fatima through the Android mobile application.
                    </p>
                </div>

                <div class="relative mx-auto w-full max-w-md lg:max-w-none" role="img" aria-label="Illustration showing the Bantay Fatima web dashboard and Android application connected through one shared system">
                    <div class="pointer-events-none absolute -top-10 -right-8 h-40 w-40 rounded-full bg-sky/10 blur-2xl" aria-hidden="true"></div>
                    <div class="pointer-events-none absolute -bottom-10 -left-8 h-48 w-48 rounded-full bg-teal/10 blur-2xl" aria-hidden="true"></div>

                    <div class="relative rounded-2xl border border-navy/10 bg-white p-4 shadow-lg sm:p-5">
                        <div class="flex items-center gap-1.5">
                            <span class="h-2.5 w-2.5 rounded-full bg-red-urgent/60" aria-hidden="true"></span>
                            <span class="h-2.5 w-2.5 rounded-full bg-amber/60" aria-hidden="true"></span>
                            <span class="h-2.5 w-2.5 rounded-full bg-teal/60" aria-hidden="true"></span>
                            <span class="ml-2 text-xs font-semibold text-navy/50">Bantay Fatima — Web Portal</span>
                        </div>

                        <div class="mt-4 grid grid-cols-3 gap-3">
                            <div class="col-span-2 rounded-xl bg-surface p-3">
                                <div class="flex items-center justify-between">
                                    <span class="text-xs font-semibold text-navy/60">Community Map</span>
                                    <x-icon name="pin" class="h-4 w-4 text-sky" />
                                </div>
                                <div class="relative mt-3 h-20 rounded-lg bg-white ring-1 ring-navy/5">
                                    <span class="absolute left-6 top-5 h-2.5 w-2.5 rounded-full bg-sky" aria-hidden="true"></span>
                                    <span class="absolute left-16 top-9 h-2.5 w-2.5 rounded-full bg-amber" aria-hidden="true"></span>
                                    <span class="absolute left-28 top-4 h-2.5 w-2.5 rounded-full bg-teal" aria-hidden="true"></span>
                                </div>
                            </div>
                            <div class="flex flex-col gap-3">
                                <div class="flex-1 rounded-xl bg-teal/10 p-3">
                                    <span class="block h-2 w-8 rounded-full bg-teal/40"></span>
                                    <span class="mt-2 block h-2 w-10 rounded-full bg-teal/30"></span>
                                </div>
                                <div class="flex-1 rounded-xl bg-sky/10 p-3">
                                    <span class="block h-2 w-8 rounded-full bg-sky/40"></span>
                                    <span class="mt-2 block h-2 w-10 rounded-full bg-sky/30"></span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="relative z-10 -mt-8 ml-auto w-36 rounded-[1.75rem] border-4 border-navy bg-white p-2 shadow-xl sm:w-44">
                        <div class="mx-auto h-1.5 w-10 rounded-full bg-navy/20"></div>
                        <div class="mt-2 rounded-xl bg-surface p-3">
                            <div class="flex items-center gap-1.5">
                                <x-logo size="sm" />
                                <span class="text-[10px] font-bold text-navy">Bantay Fatima</span>
                            </div>
                            <div class="mt-2.5 rounded-lg bg-white p-2 ring-1 ring-navy/5">
                                <span class="block text-[9px] font-semibold text-navy/50">Sample Report</span>
                                <span class="mt-1 block text-[11px] font-semibold text-navy">Clogged Drainage</span>
                                <span class="mt-1.5 inline-flex items-center gap-1 rounded-full bg-amber/15 px-2 py-0.5 text-[9px] font-semibold text-navy">
                                    <span class="h-1.5 w-1.5 rounded-full bg-amber" aria-hidden="true"></span> Pending
                                </span>
                            </div>
                        </div>
                    </div>

                    <span class="pointer-events-none absolute left-[40%] top-[42%] z-20 hidden h-9 w-9 items-center justify-center rounded-full bg-teal text-white shadow-lg sm:flex" aria-hidden="true">
                        <x-icon name="sync" class="h-4 w-4" />
                    </span>
                </div>
            </div>
        </section>

        {{-- ================= ABOUT ================= --}}
        <section id="about" class="bg-teal py-16 sm:py-20">
            <div class="wrap grid items-center gap-12 lg:grid-cols-2 lg:gap-16">
                <div class="order-2 lg:order-1">
                    <div class="relative mx-auto w-full max-w-xs" role="img" aria-label="Interface preview of the Bantay Fatima Android application showing the home menu and a sample report status">
                        <div class="rounded-[2.5rem] border-[6px] border-navy bg-white p-3 shadow-xl">
                            <div class="mx-auto h-1.5 w-16 rounded-full bg-navy/15"></div>

                            <div class="mt-3 rounded-2xl bg-surface p-4">
                                <div class="flex items-center gap-2">
                                    <x-logo size="sm" />
                                    <span class="text-sm font-bold text-navy">Welcome to Bantay Fatima</span>
                                </div>

                                <div aria-hidden="true" class="mt-4 grid grid-cols-2 gap-2.5">
                                    <div class="flex flex-col items-start gap-2 rounded-xl bg-white p-3 ring-1 ring-navy/5">
                                        <x-icon name="pin" class="h-5 w-5 text-sky" />
                                        <span class="text-xs font-semibold text-navy">Report an Issue</span>
                                    </div>
                                    <div class="flex flex-col items-start gap-2 rounded-xl bg-white p-3 ring-1 ring-navy/5">
                                        <x-icon name="timeline" class="h-5 w-5 text-teal" />
                                        <span class="text-xs font-semibold text-navy">My Reports</span>
                                    </div>
                                    <div class="flex flex-col items-start gap-2 rounded-xl bg-white p-3 ring-1 ring-navy/5">
                                        <x-icon name="megaphone" class="h-5 w-5 text-sky" />
                                        <span class="text-xs font-semibold text-navy">Announcements</span>
                                    </div>
                                    <div class="flex flex-col items-start gap-2 rounded-xl bg-white p-3 ring-1 ring-navy/5">
                                        <x-icon name="phone" class="h-5 w-5 text-red-urgent" />
                                        <span class="text-xs font-semibold text-navy">Emergency Hotlines</span>
                                    </div>
                                    <div class="col-span-2 flex items-center gap-2 rounded-xl bg-white p-3 ring-1 ring-navy/5">
                                        <x-icon name="chat" class="h-5 w-5 text-teal" />
                                        <span class="text-xs font-semibold text-navy">Barangay Assistant</span>
                                    </div>
                                </div>

                                <div class="mt-4 rounded-xl bg-white p-3 ring-1 ring-navy/5">
                                    <span class="text-[10px] font-semibold uppercase tracking-wide text-navy/50">Sample Report</span>
                                    <p class="mt-1 text-sm font-semibold text-navy">Clogged Drainage</p>
                                    <p class="mt-0.5 flex items-center gap-1 text-xs text-navy/60">
                                        <x-icon name="pin" class="h-3.5 w-3.5" /> Purok 10A
                                    </p>
                                    <span class="mt-2 inline-flex items-center gap-1.5 rounded-full bg-sky/15 px-2.5 py-1 text-xs font-semibold text-navy">
                                        <span class="h-1.5 w-1.5 rounded-full bg-sky" aria-hidden="true"></span> In Progress
                                    </span>
                                </div>
                            </div>
                        </div>

                        <p class="mt-4 flex items-center justify-center gap-2 text-center text-xs font-semibold uppercase tracking-wide text-white/70">
                            <x-icon name="eye" class="h-4 w-4" /> Interface Preview Only
                        </p>
                    </div>
                </div>

                <div class="order-1 lg:order-2">
                    <h2 class="text-3xl font-bold text-white sm:text-4xl">Built for Responsive Barangay Governance</h2>

                    <p class="mt-5 text-base leading-relaxed text-white/80">
                        Bantay Fatima provides a consistent process for community reporting, barangay review, field response, and resident updates.
                    </p>
                    <p class="mt-4 text-base leading-relaxed text-white/80">
                        Residents and authorized staff will use mobile-friendly tools while administrators coordinate official action through the secure web portal.
                    </p>

                    <div class="mt-6 rounded-2xl bg-white/10 p-5">
                        <p class="text-sm font-medium leading-relaxed text-white">
                            "Clear information and accountable follow-through help build a safer and more responsive community."
                        </p>
                    </div>

                    <ul class="mt-8 space-y-5">
                        <li class="flex items-start gap-3">
                            <span class="inline-flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-white/15 text-white">
                                <x-icon name="pin" class="h-5 w-5" />
                            </span>
                            <div>
                                <p class="font-semibold text-white">Geo-Tagged Community Reporting</p>
                                <p class="mt-0.5 text-sm text-white/75">Attach photos and an accurate map location to a community concern.</p>
                            </div>
                        </li>
                        <li class="flex items-start gap-3">
                            <span class="inline-flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-white/15 text-white">
                                <x-icon name="timeline" class="h-5 w-5" />
                            </span>
                            <div>
                                <p class="font-semibold text-white">Report Status Tracking</p>
                                <p class="mt-0.5 text-sm text-white/75">Follow verified reports from pending action through resolution.</p>
                            </div>
                        </li>
                        <li class="flex items-start gap-3">
                            <span class="inline-flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-white/15 text-white">
                                <x-icon name="megaphone" class="h-5 w-5" />
                            </span>
                            <div>
                                <p class="font-semibold text-white">Announcements and Advisories</p>
                                <p class="mt-0.5 text-sm text-white/75">Receive official barangay announcements and community advisories.</p>
                            </div>
                        </li>
                        <li class="flex items-start gap-3">
                            <span class="inline-flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-white/15 text-white">
                                <x-icon name="phone" class="h-5 w-5" />
                            </span>
                            <div>
                                <p class="font-semibold text-white">Emergency Contact Information</p>
                                <p class="mt-0.5 text-sm text-white/75">Find important emergency contact information in one place.</p>
                            </div>
                        </li>
                        <li class="flex items-start gap-3">
                            <span class="inline-flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-white/15 text-white">
                                <x-icon name="sync" class="h-5 w-5" />
                            </span>
                            <div>
                                <p class="font-semibold text-white">Shared Web and Android Access</p>
                                <p class="mt-0.5 text-sm text-white/75">Use one shared source of official records across the platform.</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </section>

        {{-- ================= CORE FEATURES ================= --}}
        <section id="features" class="bg-white py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">What Bantay Fatima Will Provide</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Bantay Fatima brings reporting, status tracking, official updates, and emergency information together in one community platform.
                    </p>
                </div>

                <div class="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                    <x-feature-card title="Geo-Tagged Issue Reporting" icon="pin" accent="sky">
                        Submit categorized concerns with supporting photos, purok details, and a precise location.
                    </x-feature-card>

                    <x-feature-card title="Report Status Tracking" icon="timeline" accent="teal">
                        View the current status and barangay updates for every submitted report.
                    </x-feature-card>

                    <x-feature-card title="Barangay Response Management" icon="team" accent="navy">
                        Help authorized administrators coordinate validation, priorities, assignments, and response monitoring.
                    </x-feature-card>

                    <x-feature-card title="Announcements and Advisories" icon="megaphone" accent="sky">
                        Receive timely announcements, advisories, and important barangay notices.
                    </x-feature-card>

                    <x-feature-card title="Emergency Hotlines" icon="phone" accent="red-urgent">
                        Access official emergency hotlines and safety information when needed.
                    </x-feature-card>

                    <x-feature-card title="Barangay AI Assistant" icon="chat" accent="teal" badge="Planned Feature">
                        Ask questions using information limited to approved barangay knowledge sources.
                    </x-feature-card>
                </div>
            </div>
        </section>

        {{-- ================= HOW IT WORKS ================= --}}
        <section id="how-it-works" class="bg-surface py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">How Community Reporting Will Work</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Reports move through a clear process from resident submission to barangay validation and response.
                    </p>
                </div>

                <div class="relative mt-16">
                    <div class="pointer-events-none absolute left-[12.5%] right-[12.5%] top-6 hidden h-0.5 bg-navy/15 md:block" aria-hidden="true"></div>

                    <ol class="relative flex flex-col gap-10 md:flex-row md:gap-6">
                        @php
                            $steps = [
                                ['title' => 'Open the Android App', 'description' => 'Residents use their verified mobile account to access Bantay Fatima.'],
                                ['title' => 'Submit a Report', 'description' => 'Provide the concern details, supporting photos, purok, and map location.'],
                                ['title' => 'Barangay Review', 'description' => 'An authorized administrator reviews the evidence before assigning action.'],
                                ['title' => 'Track the Response', 'description' => 'Receive updates as assigned staff work toward an official resolution.'],
                            ];
                        @endphp

                        @foreach ($steps as $index => $step)
                            <li class="flex flex-1 flex-col items-center text-center">
                                <span class="relative z-10 flex h-12 w-12 items-center justify-center rounded-full bg-teal text-base font-bold text-white ring-8 ring-surface">
                                    {{ $index + 1 }}
                                </span>
                                <h3 class="mt-4 text-lg font-semibold text-navy">{{ $step['title'] }}</h3>
                                <p class="mt-2 max-w-[16rem] text-sm leading-relaxed text-navy/70">{{ $step['description'] }}</p>
                            </li>
                        @endforeach
                    </ol>
                </div>
            </div>
        </section>

        {{-- ================= REPORT STATUS PREVIEW ================= --}}
        <section class="bg-surface py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">Track Every Report</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Residents will be able to review the current state of every concern they submit.
                    </p>
                    <span class="mt-4 inline-flex items-center rounded-full bg-navy/5 px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-navy/60">
                        Sample Interface Content
                    </span>
                </div>

                <div class="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                    <x-status-card issue="Uncollected Garbage" location="Purok 5" status="pending" date="Sample date" />
                    <x-status-card issue="Clogged Drainage" location="Purok 10A" status="in-progress" date="Sample date" />
                    <x-status-card issue="Broken Streetlight" location="Purok 13" status="resolved" date="Sample date" />
                </div>
            </div>
        </section>

        {{-- ================= ANNOUNCEMENTS PREVIEW ================= --}}
        <section class="bg-white py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">Barangay Announcements and Advisories</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Published barangay information will be available to residents and staff through the Android application.
                    </p>
                </div>

                <div class="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                    <x-announcement-card title="Sample Barangay Announcement" type="info" date="Sample date">
                        Official barangay updates will clearly communicate community programs, schedules, and service notices.
                    </x-announcement-card>

                    <x-announcement-card title="Sample Safety Advisory" type="advisory" date="Sample date">
                        Safety advisories will provide timely guidance for residents during local concerns or disruptions.
                    </x-announcement-card>

                    <x-announcement-card title="Sample Community Notice" type="notice" date="Sample date">
                        Community notices will help residents stay informed about relevant barangay activities.
                    </x-announcement-card>
                </div>
            </div>
        </section>

        {{-- ================= PLATFORM INFORMATION ================= --}}
        <section id="android-app" class="bg-white py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">Bantay Fatima Platforms</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">The public website provides information while residents and staff will use the upcoming Android application.</p>
                </div>
                <div class="mt-12 grid gap-6 lg:grid-cols-3">
                    <article class="rounded-2xl bg-surface p-6 shadow-sm ring-1 ring-navy/5"><span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-teal/10 text-teal"><x-icon name="android" class="h-6 w-6" /></span><h3 class="mt-4 text-lg font-semibold text-navy">Resident Mobile Application</h3><p class="mt-2 text-sm leading-relaxed text-navy/70">Residents will submit community reports with photos and locations, track their report status, receive updates, and access barangay information through the Bantay Fatima Android application.</p></article>
                    <article class="rounded-2xl bg-surface p-6 shadow-sm ring-1 ring-navy/5"><span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-sky/10 text-sky"><x-icon name="team" class="h-6 w-6" /></span><h3 class="mt-4 text-lg font-semibold text-navy">Staff Mobile Application</h3><p class="mt-2 text-sm leading-relaxed text-navy/70">Authorized barangay staff will view assigned concerns, access report locations, provide progress updates, and submit resolution evidence through the Android application.</p></article>
                    <article class="rounded-2xl bg-surface p-6 shadow-sm ring-1 ring-navy/5"><span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-navy/10 text-navy"><x-icon name="dashboard" class="h-6 w-6" /></span><h3 class="mt-4 text-lg font-semibold text-navy">Administrator Web Portal</h3><p class="mt-2 text-sm leading-relaxed text-navy/70">Authorized Barangay Fatima administrators manage reports, users, staff assignments, advisories, emergency information, and approved documents through a secure web-based management portal.</p></article>
                </div>
                <div class="mt-8 text-center"><span class="inline-flex rounded-full bg-teal/10 px-5 py-2 text-sm font-semibold text-teal">Android App Coming Soon</span></div>
            </div>
        </section>

        {{-- ================= EMERGENCY INFORMATION ================= --}}
        <section id="emergency" class="bg-surface py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">Important Emergency Information</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Use the appropriate official hotline when immediate emergency assistance is required.
                    </p>
                </div>

                <div class="mx-auto mt-12 max-w-3xl divide-y divide-navy/10 overflow-hidden rounded-2xl bg-white shadow-sm ring-1 ring-navy/5">
                    @foreach ([
                        'Barangay Emergency Hotline',
                        'Police Assistance',
                        'Fire and Rescue',
                        'Medical Emergency',
                    ] as $contact)
                        <div class="flex flex-wrap items-center justify-between gap-4 p-5">
                            <div class="flex items-center gap-3">
                                <span class="inline-flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-red-urgent/10 text-red-urgent">
                                    <x-icon name="phone" class="h-5 w-5" />
                                </span>
                                <span class="text-sm font-semibold text-navy sm:text-base">{{ $contact }}</span>
                            </div>
                            <span class="text-sm font-semibold text-navy/50">To be provided</span>
                        </div>
                    @endforeach
                </div>

                <div class="mx-auto mt-6 flex max-w-3xl items-start gap-3 rounded-2xl border border-amber/30 bg-amber/10 p-5">
                    <x-icon name="shield-check" class="mt-0.5 h-5 w-5 shrink-0 text-red-urgent" />
                    <p class="text-sm leading-relaxed text-navy">
                        Bantay Fatima does not replace national emergency hotlines or immediate emergency-response services.
                    </p>
                </div>
            </div>
        </section>

        {{-- ================= SUPPORT ================= --}}
        <section id="support" class="bg-white py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">Need Help Using Bantay Fatima?</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Contact Barangay Fatima for account assistance, accessibility concerns, or questions about published information.
                    </p>
                </div>

                <div class="mx-auto mt-12 grid max-w-4xl gap-6 sm:grid-cols-3">
                    <a href="#" class="flex flex-col gap-3 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 transition-shadow hover:shadow-md">
                        <span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-sky/10 text-sky">
                            <x-icon name="help" class="h-6 w-6" />
                        </span>
                        <span class="text-lg font-semibold text-navy">Frequently Asked Questions</span>
                        <p class="text-sm leading-relaxed text-navy/70">Find answers about reporting, status updates, privacy, and platform access.</p>
                    </a>

                    <a href="#" class="flex flex-col gap-3 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 transition-shadow hover:shadow-md">
                        <span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-teal/10 text-teal">
                            <x-icon name="mail" class="h-6 w-6" />
                        </span>
                        <span class="text-lg font-semibold text-navy">Contact Barangay Support</span>
                        <p class="text-sm leading-relaxed text-navy/70">Contact Barangay Fatima for assistance with the application or public information.</p>
                    </a>

                    <a href="#" class="flex flex-col gap-3 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 transition-shadow hover:shadow-md">
                        <span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-navy/10 text-navy">
                            <x-icon name="book" class="h-6 w-6" />
                        </span>
                        <span class="text-lg font-semibold text-navy">System User Guide</span>
                        <p class="text-sm leading-relaxed text-navy/70">Read guidance for using Bantay Fatima safely and correctly.</p>
                    </a>
                </div>
            </div>
        </section>

        {{-- ================= CALL TO ACTION ================= --}}
        <section class="bg-navy py-20 sm:py-24">
            <div class="wrap flex flex-col items-center gap-6 text-center">
                <h2 class="max-w-2xl text-3xl font-bold text-white sm:text-4xl">A Safer and More Connected Barangay Fatima</h2>
                <p class="max-w-xl text-base leading-relaxed text-white/70 sm:text-lg">
                    Learn how Bantay Fatima will support reporting, barangay coordination, and community updates.
                </p>
                <div class="mt-2 flex w-full flex-col gap-3 sm:w-auto sm:flex-row">
                    <a href="#how-it-works" class="inline-flex min-h-[44px] w-full items-center justify-center rounded-xl bg-teal px-8 text-sm font-semibold text-white transition-colors hover:bg-teal/90 sm:w-auto">
                        Learn How It Works
                    </a>
                    <a href="#android-app" class="inline-flex min-h-[44px] w-full items-center justify-center rounded-xl bg-white px-8 text-sm font-semibold text-navy transition-colors hover:bg-white/90 sm:w-auto">
                        Android App Coming Soon
                    </a>
                </div>
            </div>
        </section>
    </main>

    <x-footer />
</x-layouts.app>
