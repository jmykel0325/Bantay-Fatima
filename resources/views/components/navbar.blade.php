@php
    $navLinks = [
        ['label' => 'Home', 'href' => '#home'],
        ['label' => 'About', 'href' => '#about'],
        ['label' => 'Features', 'href' => '#features'],
        ['label' => 'How It Works', 'href' => '#how-it-works'],
        ['label' => 'Android App', 'href' => '#android-app'],
        ['label' => 'Emergency Information', 'href' => '#emergency'],
        ['label' => 'Support', 'href' => '#support'],
    ];
@endphp

<header class="sticky top-0 z-50 border-b border-navy/10 bg-white/95 backdrop-blur">
    <div class="wrap flex h-16 items-center justify-between gap-4 sm:h-20">
        <a href="{{ route('home') }}#home" class="rounded-lg">
            <x-brand-lockup theme="light" size="sm" />
        </a>

        <nav aria-label="Primary" class="hidden items-center gap-1 lg:flex">
            @foreach ($navLinks as $link)
                <a href="{{ $link['href'] }}" class="rounded-lg px-3 py-2 text-sm font-semibold text-navy/80 transition-colors hover:bg-surface hover:text-navy">{{ $link['label'] }}</a>
            @endforeach
        </nav>

        <button
            type="button"
            id="mobile-menu-button"
            aria-expanded="false"
            aria-controls="mobile-menu"
            class="inline-flex h-11 w-11 items-center justify-center rounded-lg border border-navy/15 text-navy lg:hidden"
        >
            <span class="sr-only">Toggle navigation menu</span>
            <x-icon name="menu" class="h-6 w-6" />
        </button>
    </div>

    <div id="mobile-menu" class="hidden border-t border-navy/10 bg-white lg:hidden">
        <nav aria-label="Mobile" class="wrap flex flex-col gap-1 py-4">
            @foreach ($navLinks as $link)
                <a href="{{ $link['href'] }}" class="rounded-lg px-3 py-3 text-base font-semibold text-navy/80 hover:bg-surface hover:text-navy">{{ $link['label'] }}</a>
            @endforeach
        </nav>
    </div>
</header>
