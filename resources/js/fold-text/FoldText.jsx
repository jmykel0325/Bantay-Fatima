import { useEffect, useMemo, useRef } from 'react';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';

import './FoldText.css';

gsap.registerPlugin(ScrollTrigger);

const HINGE_CONFIG = {
    top: { origin: '50% 0%', rotateX: -92, rotateY: 0 },
    bottom: { origin: '50% 100%', rotateX: 92, rotateY: 0 },
    left: { origin: '0% 50%', rotateX: 0, rotateY: 92 },
    right: { origin: '100% 50%', rotateX: 0, rotateY: -92 },
};

const clamp = (value, min, max) => Math.min(max, Math.max(min, value));

const renderWhitespace = (value, key) =>
    value.split(/(\n)/).map((part, index) => {
        if (part === '\n') return <br key={`${key}-br-${index}`} />;
        if (!part) return null;
        return <span className="fold-text-whitespace" key={`${key}-space-${index}`}>{part.replace(/ /g, '\u00A0')}</span>;
    });

export default function FoldText({
    text = 'Design unfolds', splitBy = 'char', hinge = 'top', duration = 0.65,
    stagger = 0.045, ease = 'power3.out', perspective = 700,
    creaseShading = 0.55, trigger = 'mount', fontSize = 80,
    fontWeight = 800, color = '#f7f2e8', className = '', style = {},
}) {
    const rootRef = useRef(null);
    const timelineRef = useRef(null);
    const hingeConfig = HINGE_CONFIG[hinge] || HINGE_CONFIG.top;
    const safeCrease = clamp(creaseShading, 0, 1);
    const safePerspective = Math.max(120, perspective);

    const segments = useMemo(() => {
        const renderSegment = (content, key, split = splitBy) => (
            <span className="fold-text-segment" data-fold-split={split} key={key} style={{ '--fold-perspective': `${safePerspective}px` }}>
                <span className="fold-text-piece" data-fold-hinge={hinge} style={{ transformOrigin: hingeConfig.origin, '--fold-crease': 0 }}>
                    {content || '\u00A0'}
                </span>
            </span>
        );

        if (splitBy === 'line') {
            return text.split('\n').map((line, index) => <span className="fold-text-line" key={`line-${index}`}>{renderSegment(line, `segment-line-${index}`, 'line')}</span>);
        }
        if (splitBy === 'word') {
            return text.split(/(\s+)/).flatMap((part, index) => {
                if (!part) return [];
                return /^\s+$/.test(part) ? renderWhitespace(part, `ws-${index}`) : renderSegment(part, `segment-word-${index}`);
            });
        }
        return Array.from(text).map((char, index) => char === '\n' ? <br key={`br-${index}`} /> : renderSegment(char === ' ' ? '\u00A0' : char, `segment-char-${index}`));
    }, [text, splitBy, hinge, hingeConfig.origin, safePerspective]);

    useEffect(() => {
        const root = rootRef.current;
        if (!root) return undefined;
        const pieces = Array.from(root.querySelectorAll('.fold-text-piece'));
        const reduceMotion = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;
        const fromVars = { opacity: 0 };
        const toVars = { opacity: 1, duration: reduceMotion ? 0.01 : duration, ease: reduceMotion ? 'none' : ease, stagger: reduceMotion ? 0 : stagger, clearProps: 'willChange' };
        const killTimeline = () => { timelineRef.current?.kill(); timelineRef.current = null; gsap.killTweensOf(pieces); };
        const play = (repeat = false) => { killTimeline(); timelineRef.current = gsap.timeline({ repeat: repeat ? -1 : 0, repeatDelay: 0.75 }).fromTo(pieces, fromVars, toVars); };
        let scrollTrigger;
        let hoverHandler;

        if (trigger === 'hover') {
            gsap.set(pieces, { opacity: 1 });
            hoverHandler = () => play();
            root.addEventListener('mouseenter', hoverHandler);
        } else if (trigger === 'scroll') {
            gsap.set(pieces, fromVars);
            scrollTrigger = ScrollTrigger.create({ trigger: root, start: 'top 92%', once: true, onEnter: () => play() });
        } else {
            play(trigger === 'loop');
        }

        return () => { if (hoverHandler) root.removeEventListener('mouseenter', hoverHandler); scrollTrigger?.kill(); killTimeline(); };
    }, [text, splitBy, hinge, duration, stagger, ease, safeCrease, trigger, hingeConfig]);

    return (
        <span ref={rootRef} className={`fold-text ${className}`.trim()} style={{ '--fold-text-font-size': typeof fontSize === 'number' ? `${fontSize}px` : fontSize, '--fold-text-font-weight': fontWeight, '--fold-text-color': color, ...style }}>
            <span className="fold-text-sr-only">{text}</span>
            <span className="fold-text-visual" aria-hidden="true">{segments}</span>
        </span>
    );
}
