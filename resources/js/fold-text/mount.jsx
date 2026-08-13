import { createRoot } from 'react-dom/client';
import FoldText from './FoldText';

document.querySelectorAll('[data-fold-text]').forEach((target) => {
    const isHeadline = target.id === 'landing-fold-text';
    const text = target.textContent.trim();

    createRoot(target).render(
        <FoldText
            text={text}
            splitBy={target.dataset.foldSplit || 'word'}
            hinge="top"
            trigger="scroll"
            duration={0.65}
            stagger={isHeadline ? 0.07 : 0.035}
            ease="power3.out"
            perspective={700}
            creaseShading={0.42}
            fontSize={isHeadline ? 'clamp(2.25rem, 5vw, 3.25rem)' : 'inherit'}
            fontWeight="inherit"
            color="currentColor"
        />,
    );
});
