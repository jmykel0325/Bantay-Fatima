<x-layouts.app
    title="Bantay Fatima — Community Platform for Barangay Fatima"
    description="Bantay Fatima is a geo-tagged community issue reporting, status tracking, and barangay response management platform for Barangay Fatima, accessible on the web and through the Android application."
>
    <x-navbar />

    <main id="main-content" class="landing-page">

        {{-- ================= HERO ================= --}}
        <section id="home" class="landing-hero overflow-hidden bg-white pt-16 pb-0 sm:pt-20 lg:pt-24">
            {{-- Soft flowing wave. Blurred in-SVG so the band keeps organic edges at any width. --}}
            <svg class="hero-wave" viewBox="0 0 1200 800" preserveAspectRatio="none" aria-hidden="true" focusable="false">
                <defs>
                    <linearGradient id="hero-wave-fill" x1="0" y1="1" x2="1" y2="0">
                        <stop offset="0%" stop-color="#5c9fd0" stop-opacity="0.10" />
                        <stop offset="45%" stop-color="#447fc2" stop-opacity="0.42" />
                        <stop offset="100%" stop-color="#73b1da" stop-opacity="0.16" />
                    </linearGradient>
                    <linearGradient id="hero-wave-tail" x1="0" y1="1" x2="1" y2="0">
                        <stop offset="0%" stop-color="#5c9fd0" stop-opacity="0.05" />
                        <stop offset="60%" stop-color="#447fc2" stop-opacity="0.22" />
                        <stop offset="100%" stop-color="#73b1da" stop-opacity="0.04" />
                    </linearGradient>
                    <filter id="hero-wave-blur" x="-25%" y="-25%" width="150%" height="150%">
                        <feGaussianBlur stdDeviation="52" />
                    </filter>
                    <filter id="hero-wave-blur-soft" x="-25%" y="-25%" width="150%" height="150%">
                        <feGaussianBlur stdDeviation="78" />
                    </filter>
                </defs>

                <path
                    filter="url(#hero-wave-blur-soft)"
                    fill="url(#hero-wave-tail)"
                    d="M-80 690C180 620 360 470 610 366C840 270 1010 330 1280 236L1280 610C1010 690 840 616 610 650C390 682 200 780 -80 830Z"
                />
                <path
                    filter="url(#hero-wave-blur)"
                    fill="url(#hero-wave-fill)"
                    d="M-80 560C170 505 350 330 600 232C800 152 980 258 1280 158L1280 442C980 540 800 452 600 494C400 536 190 650 -80 730Z"
                />
            </svg>
            <div class="wrap hero-layout relative z-10 grid items-center gap-10 lg:gap-8">
                <div class="hero-copy">
                    <span class="hero-fade-up inline-flex items-center rounded-full bg-teal/10 px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-teal" style="--hero-enter-delay: 80ms">
                        OFFICIAL BARANGAY PLATFORM
                    </span>

                    <h1 class="hero-fade-up mt-5 text-4xl font-bold leading-[1.08] tracking-tight text-navy sm:text-5xl lg:text-[3.25rem]" style="--hero-enter-delay: 160ms">
                        A Safer and More Connected Barangay Fatima
                    </h1>

                    <p class="hero-fade-up mt-5 text-base leading-relaxed text-navy/70 sm:text-lg" style="--hero-enter-delay: 240ms">
                        The official platform for Barangay Fatima residents to report concerns, get updates, and stay informed — in one place.
                    </p>

                    <div class="hero-fade-up mt-8 flex flex-col gap-3 sm:flex-row" style="--hero-enter-delay: 320ms">

                        <span class="inline-flex min-h-[44px] w-full items-center justify-center gap-2 rounded-xl border border-navy/15 bg-white px-7 text-sm font-semibold text-navy sm:w-auto">
                            <img
                                src="{{ asset('assets/' . rawurlencode('Play store logo.png')) }}"
                                alt=""
                                aria-hidden="true"
                                class="-my-1 h-7 w-7 shrink-0 object-contain"
                            >
                            Get it on Google Play
                        </span>
                    </div>


                </div>

                <div class="hero-model relative flex w-full items-end justify-center">
                    <div class="hero-model-halo" aria-hidden="true"></div>
                    <img
                        src="{{ asset('assets/Model.png') }}"
                        alt="Bantay Fatima community representative holding and pointing to a mobile phone"
                        class="hero-model-image"
                    >
                    <div id="hero-phone-mockup" class="hero-phone-exact" aria-hidden="true"></div>
                </div>
            </div>
        </section>

        {{-- ================= ABOUT ================= --}}
        <section id="about" class="landing-about overflow-hidden bg-teal py-16 sm:py-20">
            <img src="{{ asset('assets/FatimaLogo.png') }}" alt="" class="fatima-watermark" aria-hidden="true">
            <div class="section-rings" aria-hidden="true"></div>
            <div class="wrap relative z-10 grid items-center gap-12 lg:grid-cols-2 lg:gap-16">
                <div id="bantay-phone-mockup" class="order-2 lg:order-1">
                    {{--
                        Replica of the Android guest landing screen
                        (ui/public/LandingPageScreen.kt): navy header, overlapping
                        search field, eight service tiles, latest updates, notice
                        and the guest bottom navigation.
                    --}}
                    <div class="relative mx-auto w-full max-w-xs" role="img" aria-label="Interface preview of the Bantay Fatima Android application home screen showing barangay services, latest updates and the bottom navigation">
                        <div class="overflow-hidden rounded-[2.5rem] border-[6px] border-navy bg-surface pt-3 shadow-xl">
                            <div class="mx-auto h-1.5 w-16 rounded-full bg-navy/15"></div>

                            <div aria-hidden="true" class="mt-3">
                                {{-- Header + search --}}
                                <div class="relative pb-7">
                                    <div class="rounded-b-[1.75rem] bg-navy px-4 pb-8 pt-4">
                                        <div class="flex items-center gap-2">
                                            <x-logo size="sm" class="rounded-lg" />
                                            <span class="flex-1 text-sm font-bold text-white">Bantay Fatima</span>
                                            <x-icon name="bell" class="h-4 w-4 text-white" />
                                            <x-icon name="user" class="h-4 w-4 text-white" />
                                        </div>
                                        <div class="mt-3 flex items-center gap-2">
                                            <span class="inline-flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-teal">
                                                <x-icon name="megaphone" class="h-4 w-4 text-white" />
                                            </span>
                                            <div>
                                                <p class="text-xs font-semibold text-white">Good day!</p>
                                                <p class="text-[10px] text-white/70">How can Bantay Fatima assist you?</p>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="absolute inset-x-4 bottom-0 flex items-center gap-2 rounded-2xl border border-navy/10 bg-white px-3 py-2.5 shadow-md">
                                        <x-icon name="search" class="h-4 w-4 text-navy/50" />
                                        <span class="truncate text-[10px] text-navy/50">Search services, announcements, reports...</span>
                                    </div>
                                </div>

                                {{-- Services --}}
                                <div class="mx-3 rounded-2xl border border-navy/10 bg-white p-2">
                                    <div class="grid grid-cols-4 gap-1.5">
                                        @foreach ([
                                            ['edit-note', 'Report Concern', 'text-teal', 'bg-teal/10'],
                                            ['reports', 'My Reports', 'text-teal', 'bg-teal/10'],
                                            ['megaphone', 'Announcements', 'text-sky', 'bg-sky/10'],
                                            ['siren', 'Emergency', 'text-red-urgent', 'bg-red-urgent/10'],
                                            ['help', 'How It Works', 'text-amber', 'bg-amber/10'],
                                            ['chat', 'Assistant', 'text-sky', 'bg-sky/10'],
                                            ['headset', 'Support', 'text-navy', 'bg-navy/10'],
                                            ['dashboard', 'View All', 'text-navy/60', 'bg-navy/5'],
                                        ] as [$icon, $label, $tint, $tile])
                                            <div class="flex aspect-[4/5] flex-col items-center justify-between rounded-xl {{ $tile }} px-1 py-2">
                                                <x-icon name="{{ $icon }}" class="h-5 w-5 {{ $tint }}" />
                                                <span class="text-center text-[7px] font-medium leading-tight text-navy">{{ $label }}</span>
                                            </div>
                                        @endforeach
                                    </div>
                                </div>

                                {{-- Latest updates --}}
                                <div class="mx-3 mt-3 rounded-2xl border border-navy/10 bg-white p-3">
                                    <div class="flex items-center justify-between">
                                        <span class="text-xs font-semibold text-navy">Latest Barangay Updates</span>
                                        <span class="rounded-lg border border-teal/40 px-2 py-0.5 text-[8px] font-semibold text-teal">View All</span>
                                    </div>
                                    <div class="mt-1 divide-y divide-navy/10">
                                        @foreach ([
                                            ['article', 'Water interruption advisory', 'Advisory', 'text-teal'],
                                            ['siren', 'Road clearing along Purok 10A', 'Urgent', 'text-red-urgent'],
                                        ] as [$icon, $title, $type, $tint])
                                            <div class="flex items-center gap-2 py-2">
                                                <span class="inline-flex h-7 w-7 shrink-0 items-center justify-center rounded-lg bg-teal/10">
                                                    <x-icon name="{{ $icon }}" class="h-4 w-4 {{ $tint }}" />
                                                </span>
                                                <div class="min-w-0 flex-1">
                                                    <p class="truncate text-[10px] font-medium text-navy">{{ $title }}</p>
                                                    <p class="text-[9px] text-navy/50">{{ $type }}</p>
                                                </div>
                                            </div>
                                        @endforeach
                                    </div>
                                </div>

                                {{-- Public notice --}}
                                <div class="mx-3 mt-3 flex items-center gap-2 rounded-xl border border-navy/10 bg-white/70 p-2.5">
                                    <x-icon name="info" class="h-4 w-4 shrink-0 text-teal" />
                                    <p class="flex-1 text-[9px] leading-snug text-navy">For urgent situations, contact the appropriate emergency service.</p>
                                    <span class="text-[9px] font-semibold text-teal">Hotlines</span>
                                </div>

                                {{-- Bottom navigation --}}
                                <div class="mt-3 grid grid-cols-5 items-end border-t border-navy/10 bg-white px-1 pb-2 pt-1.5">
                                    @foreach ([
                                        ['home', 'Home', true, false],
                                        ['megaphone', 'Updates', false, false],
                                        ['plus', 'Report a Problem', false, true],
                                        ['chat', 'Assistant', false, false],
                                        ['user', 'Account', false, false],
                                    ] as [$icon, $label, $active, $emphasized])
                                        <div class="flex flex-col items-center gap-0.5">
                                            @if ($emphasized)
                                                <span class="inline-flex h-8 w-8 items-center justify-center rounded-full bg-teal shadow-md">
                                                    <x-icon name="plus" class="h-4 w-4 text-white" />
                                                </span>
                                            @else
                                                <span class="inline-flex h-5 w-9 items-center justify-center rounded-full {{ $active ? 'bg-teal/15' : '' }}">
                                                    <x-icon name="{{ $icon }}" class="h-4 w-4 {{ $active ? 'text-teal' : 'text-navy/50' }}" />
                                                </span>
                                            @endif
                                            <span class="text-center text-[7px] leading-tight {{ $active ? 'font-semibold text-teal' : 'text-navy/50' }}">{{ $label }}</span>
                                        </div>
                                    @endforeach
                                </div>
                            </div>
                        </div>

                        <p class="mt-4 flex items-center justify-center gap-2 text-center text-xs font-semibold uppercase tracking-wide text-white/70">
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
                    </ul>
                </div>
            </div>
        </section>

        {{-- ================= CORE FEATURES ================= --}}
        <section id="features" class="landing-features overflow-hidden bg-white py-16 sm:py-20">
            <div class="section-dots" aria-hidden="true"></div>
            <div class="wrap relative z-10">
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
        <section id="how-it-works" class="landing-process overflow-hidden bg-surface py-16 sm:py-20">
            <div class="wrap relative z-10">
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
        <section class="landing-reports bg-white py-16 sm:py-20">
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
        <section class="landing-announcements bg-surface py-16 sm:py-20">
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

        {{-- ================= EMERGENCY INFORMATION ================= --}}
        <section id="emergency" class="landing-emergency bg-white py-16 sm:py-20">
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
        <section id="support" class="landing-support bg-surface py-16 sm:py-20">
            <div class="wrap">
                <div class="mx-auto max-w-2xl text-center">
                    <h2 class="text-3xl font-bold text-navy sm:text-4xl">Need Help Using Bantay Fatima?</h2>
                    <p class="mt-4 text-base leading-relaxed text-navy/70 sm:text-lg">
                        Contact Barangay Fatima for account assistance, accessibility concerns, or questions about published information.
                    </p>
                </div>

                <div class="mx-auto mt-12 grid max-w-4xl gap-6 sm:grid-cols-2 lg:grid-cols-3">
                    <a href="#" class="landing-card flex h-full flex-col gap-3 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
                        <span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-sky/10 text-sky">
                            <x-icon name="help" class="h-6 w-6" />
                        </span>
                        <span class="text-lg font-semibold text-navy">Frequently Asked Questions</span>
                        <p class="text-sm leading-relaxed text-navy/70">Find answers about reporting, status updates, privacy, and platform access.</p>
                    </a>

                    <a href="#" class="landing-card flex h-full flex-col gap-3 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
                        <span class="inline-flex h-12 w-12 items-center justify-center rounded-xl bg-teal/10 text-teal">
                            <x-icon name="mail" class="h-6 w-6" />
                        </span>
                        <span class="text-lg font-semibold text-navy">Contact Barangay Support</span>
                        <p class="text-sm leading-relaxed text-navy/70">Contact Barangay Fatima for assistance with the application or public information.</p>
                    </a>

                    <a href="#" class="landing-card flex h-full flex-col gap-3 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5">
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
        <section class="landing-cta overflow-hidden bg-navy py-20 sm:py-24">
            <div class="cta-glow" aria-hidden="true"></div>
            <div class="wrap relative z-10 flex flex-col items-center gap-6 text-center">
                <h2 class="max-w-2xl text-3xl font-bold text-white sm:text-4xl">A Safer and More Connected Barangay Fatima</h2>
                <p class="max-w-xl text-base leading-relaxed text-white/70 sm:text-lg">
                    Learn how Bantay Fatima will support reporting, barangay coordination, and community updates.
                </p>
                <div class="mt-2 flex w-full flex-col gap-3 sm:w-auto sm:flex-row">
                    <a href="#how-it-works" class="inline-flex min-h-[44px] w-full items-center justify-center rounded-xl bg-teal px-8 text-sm font-semibold text-white transition-colors hover:bg-teal/90 sm:w-auto">
                        Learn How It Works
                    </a>
                    <span class="inline-flex min-h-[44px] w-full items-center justify-center gap-2 rounded-xl border border-white/25 px-8 text-sm font-semibold text-white sm:w-auto">
                        <span class="h-2 w-2 rounded-full bg-amber" aria-hidden="true"></span>
                        Android App Coming Soon
                    </span>
                </div>
            </div>
        </section>
    </main>

    <x-footer />
</x-layouts.app>
