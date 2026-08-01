{{-- Developer note: This draft policy must be reviewed by the Barangay Fatima administration or a qualified privacy officer before production publication. --}}
<x-layouts.app title="Privacy Policy — Bantay Fatima">
    <x-navbar />
    <main id="main-content" class="bg-surface py-12 sm:py-16">
        <article class="wrap max-w-4xl">
            <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 sm:p-10">
                <p class="text-sm font-semibold uppercase tracking-wide text-teal">Bantay Fatima</p>
                <h1 class="mt-2 text-3xl font-bold text-navy sm:text-4xl">Privacy Policy</h1>
                <p class="mt-3 text-sm text-navy/55">Last updated: August 2, 2026</p>
                <p class="mt-6 leading-relaxed text-navy/75">This is a development-stage privacy notice intended for administrative review. It describes the planned handling of information without claiming legal compliance that has not been formally assessed.</p>

                @php
                    $sections = [
                        ['Information Collected', 'Bantay Fatima may collect account details, authentication data, report content, photos, locations, and technical session information needed to operate the service.'],
                        ['Account Information', 'Account records include the resident’s name, optional middle name and suffix, verified phone number, role, status, and encrypted password representation.'],
                        ['Email Verification', 'A six-digit code is sent to the submitted email address before account creation and during password recovery. Codes are hashed, expire quickly, and have limited attempts.'],
                        ['Phone Number Information', 'A Philippine mobile number is required as resident account information. The current email-verification process does not independently verify ownership of that phone number.'],
                        ['Photos and Report Descriptions', 'Reports may contain photos and descriptions provided by residents. Users should submit only information relevant to the reported concern.'],
                        ['Location and Geo-Tagging Information', 'Location information may be attached to reports to help barangay personnel identify and assess the concern.'],
                        ['How Information Is Used', 'Information is used to authenticate users, receive and track reports, provide updates, administer accounts, prevent abuse, and improve service reliability.'],
                        ['Who Can Access the Information', 'Authorized barangay personnel and approved system administrators may access information according to their assigned responsibilities.'],
                        ['Data Retention', 'Retention periods must be approved by the Barangay Fatima administration. Records should not be kept longer than necessary for approved operational purposes.'],
                        ['Data Security', 'The planned safeguards include password hashing, hashed email verification codes, encrypted pending-registration data, access controls, secure sessions, rate limits, and encrypted HTTPS transport.'],
                        ['User Rights', 'Residents may request access to or correction of their account information through official barangay channels, subject to approved identity-verification procedures.'],
                        ['Account Deletion Requests', 'Deletion requests should be submitted to the Barangay Fatima administration. Some records may need to be retained under an approved operational or legal policy.'],
                        ['Cookies and Sessions', 'The web application uses cookies and server sessions for authentication, security, CSRF protection, and remembered login preferences.'],
                        ['Third-Party Services', 'Service providers may process limited information when needed for authentication, infrastructure, or other approved platform functions.'],
                        ['Email Delivery Services', 'Account and password-recovery codes are delivered through the configured email provider, such as Gmail or Google Workspace. The email address and delivery metadata may be processed by that provider to deliver messages and prevent abuse.'],
                        ['Policy Updates', 'Material updates should include a revised date and appropriate notice after administrative or privacy review.'],
                        ['Barangay Contact Information', 'Privacy questions and requests may be directed to the Barangay Fatima administration through its official contact channels.'],
                    ];
                @endphp

                <div class="mt-9 space-y-8">
                    @foreach ($sections as [$heading, $content])
                        <section><h2 class="text-xl font-bold text-navy">{{ $heading }}</h2><p class="mt-2 leading-relaxed text-navy/70">{{ $content }}</p></section>
                    @endforeach
                </div>
            </div>
        </article>
    </main>
    <x-footer />
</x-layouts.app>
