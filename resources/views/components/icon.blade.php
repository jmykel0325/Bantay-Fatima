@props(['name'])

@php
    $paths = [
        'pin' => '<path d="M12 21s7-6.1 7-11.5A7 7 0 0 0 5 9.5C5 14.9 12 21 12 21Z"/><circle cx="12" cy="9.5" r="2.4"/>',
        'timeline' => '<circle cx="5" cy="6" r="2"/><circle cx="12" cy="12" r="2"/><circle cx="19" cy="18" r="2"/><path d="M6.7 7.3 10.3 10.7M13.7 13.3 17.3 16.7"/>',
        'team' => '<circle cx="9" cy="8" r="3"/><path d="M3.5 20a5.5 5.5 0 0 1 11 0"/><circle cx="17.5" cy="9" r="2.4"/><path d="M15 20a4.8 4.8 0 0 1 6.5-4.4"/>',
        'megaphone' => '<path d="M3 11v2a2 2 0 0 0 2 2h1l2 5h2l-1.3-5H12l7 3V6l-7 3H6a2 2 0 0 0-2 2Z"/><path d="M19 9v6"/>',
        'phone' => '<path d="M6.6 10.8a13.9 13.9 0 0 0 6.6 6.6l2.2-2.2a1.3 1.3 0 0 1 1.3-.3c1.2.4 2.5.6 3.8.6a1.3 1.3 0 0 1 1.3 1.3V20a1.3 1.3 0 0 1-1.3 1.3C10.9 21.3 2.7 13.1 2.7 4.5A1.3 1.3 0 0 1 4 3.2h3.3A1.3 1.3 0 0 1 8.6 4.5c0 1.3.2 2.6.6 3.8a1.3 1.3 0 0 1-.3 1.3Z"/>',
        'chat' => '<path d="M4 5h16v11H8l-4 4V5Z"/><path d="M8 9h8M8 12.5h5"/>',
        'browser' => '<rect x="3" y="4.5" width="18" height="15" rx="2"/><path d="M3 9h18"/><circle cx="6" cy="6.7" r="0.6"/><circle cx="8.4" cy="6.7" r="0.6"/>',
        'android' => '<rect x="6" y="4" width="12" height="17" rx="2.5"/><path d="M9 8h6M9 17.5h6"/>',
        'menu' => '<path d="M4 7h16M4 12h16M4 17h16"/>',
        'close' => '<path d="M6 6l12 12M18 6 6 18"/>',
        'eye' => '<path d="M2.5 12S6 5.5 12 5.5 21.5 12 21.5 12 18 18.5 12 18.5 2.5 12 2.5 12Z"/><circle cx="12" cy="12" r="2.6"/>',
        'chevron-down' => '<path d="M6 9l6 6 6-6"/>',
        'sync' => '<path d="M3.5 12a8.5 8.5 0 0 1 14.5-6M20.5 12a8.5 8.5 0 0 1-14.5 6"/><path d="M18 3v3.5h-3.5M6 21v-3.5h3.5"/>',
        'shield-check' => '<path d="M12 3l7 3v6c0 5-3.5 7.5-7 9-3.5-1.5-7-4-7-9V6l7-3Z"/><path d="M9.2 12.2l2 2 3.6-4"/>',
        'calendar' => '<rect x="3.5" y="5" width="17" height="15" rx="2"/><path d="M3.5 9.5h17M8 3v3M16 3v3"/>',
        'check' => '<path d="M5 12.5l4.5 4.5L19 7"/>',
        'arrow-down' => '<path d="M12 4v15M6 13l6 6 6-6"/>',
        'building' => '<path d="M4 21V6l8-3 8 3v15"/><path d="M9 21v-6h6v6M9 10h.01M15 10h.01M9 14h.01M15 14h.01"/>',
        'lock' => '<rect x="5" y="10.5" width="14" height="9" rx="2"/><path d="M8 10.5V8a4 4 0 0 1 8 0v2.5"/>',
        'mail' => '<rect x="3" y="5" width="18" height="14" rx="2"/><path d="m3.5 6 8.5 7 8.5-7"/>',
        'help' => '<circle cx="12" cy="12" r="9"/><path d="M9.5 9.3a2.5 2.5 0 1 1 3.7 2.2c-.9.5-1.2 1-1.2 2"/><circle cx="12" cy="17" r="0.6"/>',
        'book' => '<path d="M4 5.5A2.5 2.5 0 0 1 6.5 3H20v15.5H6.5A2.5 2.5 0 0 0 4 21V5.5Z"/><path d="M4 18.5A2.5 2.5 0 0 1 6.5 16H20"/>',
        'life-ring' => '<circle cx="12" cy="12" r="9"/><circle cx="12" cy="12" r="4"/><path d="m6.3 6.3 3.4 3.4M17.7 6.3l-3.4 3.4M6.3 17.7l3.4-3.4M17.7 17.7l-3.4-3.4"/>',
        'home' => '<path d="M4 11.5 12 4l8 7.5"/><path d="M6 10v10h12V10"/>',
        'bell' => '<path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9Z"/><path d="M10 21h4"/>',
        'user' => '<circle cx="12" cy="8" r="4"/><path d="M4.5 21a7.5 7.5 0 0 1 15 0"/>',
        'plus' => '<path d="M12 5v14M5 12h14"/>',
        'reports' => '<path d="M6 3h9l4 4v14H6z"/><path d="M15 3v5h5M9 12h7M9 16h7"/>',
        'dashboard' => '<rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/>',
        'chart' => '<path d="M4 20V10M10 20V4M16 20v-7M22 20H2"/>',
        'settings' => '<circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.7 1.7 0 0 0 .3 1.9l.1.1-2.8 2.8-.1-.1a1.7 1.7 0 0 0-1.9-.3 1.7 1.7 0 0 0-1 1.6v.2h-4V21a1.7 1.7 0 0 0-1-1.6 1.7 1.7 0 0 0-1.9.3l-.1.1L4.2 17l.1-.1a1.7 1.7 0 0 0 .3-1.9A1.7 1.7 0 0 0 3 14H2.8v-4H3a1.7 1.7 0 0 0 1.6-1 1.7 1.7 0 0 0-.3-1.9L4.2 7 7 4.2l.1.1A1.7 1.7 0 0 0 9 4.6 1.7 1.7 0 0 0 10 3V2.8h4V3a1.7 1.7 0 0 0 1 1.6 1.7 1.7 0 0 0 1.9-.3l.1-.1L19.8 7l-.1.1a1.7 1.7 0 0 0-.3 1.9 1.7 1.7 0 0 0 1.6 1h.2v4H21a1.7 1.7 0 0 0-1.6 1Z"/>',
        'database' => '<ellipse cx="12" cy="5" rx="8" ry="3"/><path d="M4 5v7c0 1.7 3.6 3 8 3s8-1.3 8-3V5M4 12v7c0 1.7 3.6 3 8 3s8-1.3 8-3v-7"/>',
        'clock' => '<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3.5 2"/>',
        'download' => '<path d="M12 3v12M7 10l5 5 5-5"/><path d="M4 19h16"/>',
        // Mirrors of the Android landing screen icons (LandingPageScreen.kt).
        'search' => '<circle cx="11" cy="11" r="6.5"/><path d="m16 16 4.5 4.5"/>',
        'edit-note' => '<path d="M4 7h11M4 12h7M4 17h5"/><path d="m15.5 17.5 5-5 2.5 2.5-5 5H15.5v-2.5Z"/>',
        'article' => '<rect x="4" y="4" width="16" height="16" rx="2"/><path d="M8 9h8M8 12.5h8M8 16h5"/>',
        'headset' => '<path d="M4 14v-2a8 8 0 0 1 16 0v2"/><rect x="2.5" y="13.5" width="4" height="6" rx="1.6"/><rect x="17.5" y="13.5" width="4" height="6" rx="1.6"/><path d="M20 19.5v.5a2.5 2.5 0 0 1-2.5 2.5H13"/>',
        'info' => '<circle cx="12" cy="12" r="9"/><path d="M12 11v5.5"/><circle cx="12" cy="7.9" r="0.7"/>',
        'siren' => '<path d="M6 19v-6a6 6 0 0 1 12 0v6"/><rect x="4" y="19" width="16" height="2.5" rx="1"/><path d="M12 4V2M4.6 7 3.2 5.6M19.4 7l1.4-1.4"/>',
    ];

    $path = $paths[$name] ?? '';
@endphp

<svg {{ $attributes->merge(['class' => 'h-6 w-6', 'fill' => 'none', 'viewBox' => '0 0 24 24', 'stroke' => 'currentColor', 'stroke-width' => '1.8', 'stroke-linecap' => 'round', 'stroke-linejoin' => 'round', 'aria-hidden' => 'true']) }}>
    {!! $path !!}
</svg>
