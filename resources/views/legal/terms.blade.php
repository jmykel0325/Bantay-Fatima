<x-layouts.app title="Terms and Conditions — Bantay Fatima">
    <x-navbar />
    <main id="main-content" class="bg-surface py-12 sm:py-16">
        <article class="wrap max-w-4xl">
            <div class="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/5 sm:p-10">
                <p class="text-sm font-semibold uppercase tracking-wide text-teal">Bantay Fatima</p>
                <h1 class="mt-2 text-3xl font-bold text-navy sm:text-4xl">Terms and Conditions</h1>
                <p class="mt-3 text-sm text-navy/55">Last updated: August 2, 2026</p>
                <p class="mt-6 leading-relaxed text-navy/75">These draft terms describe the expected use of Bantay Fatima. They remain subject to review and approval by the Barangay Fatima administration.</p>

                @php
                    $sections = [
                        ['Acceptance of Terms', 'By creating an account or using the platform, users agree to follow these terms and applicable barangay policies.'],
                        ['Eligibility and Account Registration', 'Users must provide accurate account details, maintain one account, protect their password, and verify a Philippine mobile number.'],
                        ['Proper Use of the Platform', 'The platform must be used responsibly for legitimate community concerns, barangay information, and permitted resident services.'],
                        ['Community Issue Reporting', 'Reports should identify genuine community concerns and include enough accurate information for barangay personnel to assess them.'],
                        ['Prohibited Reports and Activities', 'False, abusive, threatening, discriminatory, unlawful, spam, impersonation, and intentionally misleading content is prohibited.'],
                        ['Accuracy of Submitted Information', 'Users are responsible for reviewing the accuracy of names, descriptions, locations, and other details they submit.'],
                        ['Photos and Location Information', 'Only relevant photos and locations should be submitted. Users should avoid exposing unrelated people or sensitive private information.'],
                        ['Barangay Response and Resolution', 'Submission does not guarantee a particular response time or result. Priority and resolution remain subject to barangay assessment and available resources.'],
                        ['Account Suspension', 'Accounts may be limited or suspended when needed to protect users, investigate misuse, or enforce approved platform rules.'],
                        ['Intellectual Property', 'The platform interface and official materials may not be copied or misrepresented. Users retain responsibility for content they submit.'],
                        ['Limitation of Liability', 'Bantay Fatima is not an emergency service and does not replace emergency hotlines or direct contact with authorized agencies.'],
                        ['Changes to the Terms', 'These terms may be updated after administrative review. A revised date and appropriate notice should accompany material changes.'],
                        ['Contact Information', 'Questions may be directed to the Barangay Fatima administration through its official contact channels.'],
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
