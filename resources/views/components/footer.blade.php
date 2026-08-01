<footer class="bg-navy text-white">
    <div class="wrap grid gap-10 py-16 sm:py-20 md:grid-cols-2 lg:grid-cols-4">
        <div class="md:col-span-2 lg:col-span-1">
            <x-brand-lockup theme="dark" size="sm" stack="mobile" />
            <p class="mt-4 max-w-xs text-sm leading-relaxed text-white/70">
                Geo-Tagged Community Issue Reporting and Barangay Response Management System
            </p>
            <p class="mt-4 flex items-start gap-2 text-sm text-white/70">
                <x-icon name="pin" class="mt-0.5 h-4 w-4 shrink-0" />
                Barangay Fatima, General Santos City, Philippines
            </p>
        </div>

        <nav aria-label="Quick links">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-white/50">Quick Links</h3>
            <ul class="mt-4 space-y-2.5 text-sm">
                <li><a href="#home" class="text-white/75 hover:text-white">Home</a></li>
                <li><a href="#about" class="text-white/75 hover:text-white">About</a></li>
                <li><a href="#features" class="text-white/75 hover:text-white">Features</a></li>
                <li><a href="#how-it-works" class="text-white/75 hover:text-white">How It Works</a></li>
                <li><a href="#android-app" class="text-white/75 hover:text-white">Android App</a></li>
                <li><a href="#emergency" class="text-white/75 hover:text-white">Emergency Information</a></li>
                <li><a href="#support" class="text-white/75 hover:text-white">Support</a></li>
            </ul>
        </nav>

        <nav aria-label="Platform links">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-white/50">Platforms</h3>
            <ul class="mt-4 space-y-2.5 text-sm">
                <li><a href="#android-app" class="text-white/75 hover:text-white">Android Application</a></li>
                <li><span class="text-white/75">Administrator Web Portal</span></li>
            </ul>
        </nav>

        <nav aria-label="Legal">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-white/50">Legal</h3>
            <ul class="mt-4 space-y-2.5 text-sm">
                <li><a href="{{ route('privacy') }}" class="text-white/75 hover:text-white">Privacy Notice</a></li>
                <li><a href="{{ route('terms') }}" class="text-white/75 hover:text-white">Terms and Conditions</a></li>
                <li><a href="#" class="text-white/75 hover:text-white">Accessibility</a></li>
                <li><a href="#support" class="text-white/75 hover:text-white">Support</a></li>
            </ul>
        </nav>
    </div>

    <div class="border-t border-white/10">
        <div class="wrap py-6">
            <p class="text-xs leading-relaxed text-white/60">
                <strong class="font-semibold text-white/80">Development Notice:</strong>
                Bantay Fatima is an academic capstone project currently under development. It does not replace official emergency-response services or authorized barangay decisions.
            </p>
        </div>
    </div>

    <div class="border-t border-white/10">
        <div class="wrap py-6">
            <p class="text-xs text-white/50">&copy; {{ date('Y') }} Bantay Fatima. All rights reserved.</p>
        </div>
    </div>
</footer>
