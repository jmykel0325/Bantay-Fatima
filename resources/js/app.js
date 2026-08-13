import './email-verification';

const phoneMockupSource = document.getElementById('bantay-phone-mockup');
const heroPhoneTarget = document.getElementById('hero-phone-mockup');

if (phoneMockupSource && heroPhoneTarget) {
    const phoneMockup = phoneMockupSource.cloneNode(true);
    phoneMockup.removeAttribute('id');
    phoneMockup.className = 'hero-phone-clone';
    heroPhoneTarget.appendChild(phoneMockup);
}

document.addEventListener('DOMContentLoaded', () => {
    const menuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (menuButton && mobileMenu) {
        const closeMenu = () => {
            mobileMenu.classList.add('hidden');
            menuButton.setAttribute('aria-expanded', 'false');
        };

        const openMenu = () => {
            mobileMenu.classList.remove('hidden');
            menuButton.setAttribute('aria-expanded', 'true');
        };

        menuButton.addEventListener('click', () => {
            const isOpen = menuButton.getAttribute('aria-expanded') === 'true';
            isOpen ? closeMenu() : openMenu();
        });

        mobileMenu.querySelectorAll('a').forEach((link) => {
            link.addEventListener('click', closeMenu);
        });

        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && menuButton.getAttribute('aria-expanded') === 'true') {
                closeMenu();
                menuButton.focus();
            }
        });
    }

    const togglePasswordButton = document.getElementById('toggle-password');
    const passwordInput = document.getElementById('password');

    if (togglePasswordButton && passwordInput) {
        togglePasswordButton.addEventListener('click', () => {
            const isPassword = passwordInput.getAttribute('type') === 'password';
            passwordInput.setAttribute('type', isPassword ? 'text' : 'password');
            togglePasswordButton.setAttribute('aria-pressed', String(isPassword));
            togglePasswordButton.textContent = isPassword ? 'Hide' : 'Show';
        });
    }

    const loginForm = document.getElementById('login-form');
    loginForm?.addEventListener('submit', () => {
        const submitButton = loginForm.querySelector('button[type="submit"]');
        if (submitButton) {
            submitButton.disabled = true;
            submitButton.textContent = 'Logging in…';
        }
    });

    const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    const revealElements = document.querySelectorAll(
        'main section:not(#home) .wrap > *, main section:not(#home) .grid > *, footer .wrap > *',
    );

    if (!prefersReducedMotion && revealElements.length > 0) {
        const revealVariants = [
            'scroll-reveal-up',
            'scroll-reveal-left',
            'scroll-reveal-right',
            'scroll-reveal-scale',
            'scroll-reveal-tilt',
        ];

        revealElements.forEach((element, index) => {
            element.classList.add('scroll-reveal');
            element.classList.add(revealVariants[Math.floor(Math.random() * revealVariants.length)]);
            element.style.setProperty('--reveal-delay', `${(index % 4) * 70}ms`);
        });

        const revealObserver = new IntersectionObserver(
            (entries, observer) => {
                entries.forEach((entry) => {
                    if (!entry.isIntersecting) return;

                    entry.target.classList.add('is-visible');
                    observer.unobserve(entry.target);
                });
            },
            {
                threshold: 0.12,
                rootMargin: '0px 0px -8% 0px',
            },
        );

        revealElements.forEach((element) => revealObserver.observe(element));
    }
});
