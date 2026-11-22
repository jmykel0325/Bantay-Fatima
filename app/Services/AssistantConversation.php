<?php

namespace App\Services;

/**
 * Conversational turns the assistant may answer without the knowledge base.
 *
 * Retrieval alone cannot hold a conversation. A resident who opens with "Hi" has
 * asked nothing retrievable, so the search returns no approved chunk and the
 * assistant refuses — which reads as a broken assistant rather than a careful one.
 *
 * This handles only the turns that carry no factual claim: greetings, thanks,
 * farewells, and questions about the assistant itself. Everything else falls
 * through to retrieval, so a question about barangay policy is still answered from
 * approved sources or refused. Nothing here states a fact about the barangay.
 */
class AssistantConversation
{
    /**
     * @return array{available: bool, answer: string, sources: array}|null
     *         null when the message is not small talk and must go to retrieval.
     */
    public static function resolve(string $question): ?array
    {
        $text = trim(mb_strtolower($question));
        $normalised = trim(preg_replace('/\s+/u', ' ', preg_replace('/[^\pL\pN\s]+/u', ' ', $text)));

        if ($normalised === '') {
            return null;
        }

        // Only short utterances are treated as conversational. "Hi, how do I request
        // a barangay clearance?" carries a real question and must reach retrieval,
        // even though it opens with a greeting.
        if (count(explode(' ', $normalised)) > 6) {
            return null;
        }

        foreach (self::intents() as [$patterns, $answer]) {
            foreach ($patterns as $pattern) {
                if (preg_match($pattern, $normalised) === 1) {
                    return ['available' => true, 'answer' => $answer, 'sources' => []];
                }
            }
        }

        return null;
    }

    /**
     * Intent patterns paired with their reply.
     *
     * Cebuano and Tagalog forms are included alongside English because residents of
     * Barangay Fatima open in whichever comes naturally.
     */
    private static function intents(): array
    {
        return [
            [
                [
                    '/^(hi|hello|hey|yo|hoy)$/u',
                    '/^(hi|hello|hey)\s+(there|po|assistant)$/u',
                    '/^(good\s+(morning|afternoon|evening|day))/u',
                    '/^(magandang\s+(umaga|hapon|gabi|araw))/u',
                    '/^(maayong\s+(buntag|hapon|gabii|adlaw))/u',
                    '/^(kumusta|kamusta)(\s+(ka|po|kayo))?$/u',
                ],
                'Good day. I can help with Barangay Fatima services, announcements, advisories, and emergency information. What would you like to know?',
            ],
            [
                [
                    '/^(how\s+are\s+you)/u',
                    '/^(are\s+you\s+(there|ok|okay))/u',
                ],
                'I am here and ready to help. Ask me about barangay services, requirements, announcements, or emergency hotlines.',
            ],
            [
                [
                    '/(thank\s*you|thanks|thank\s*u)/u',
                    '/(salamat)/u',
                ],
                'You are welcome. Let me know if you need anything else about Barangay Fatima.',
            ],
            [
                [
                    '/^(bye|goodbye|good\s+bye|see\s+you)/u',
                    '/^(paalam|babay)/u',
                ],
                'Thank you for using Bantay Fatima. Stay safe.',
            ],
            [
                [
                    '/(who\s+are\s+you|what\s+are\s+you)/u',
                    '/(your\s+name)/u',
                    '/^(ano\s+ka|sino\s+ka)/u',
                ],
                'I am the Bantay Fatima assistant. I answer using approved Barangay Fatima documents, advisories, and emergency information only.',
            ],
            [
                [
                    '/(what\s+can\s+you\s+do|how\s+can\s+you\s+help|what\s+do\s+you\s+do)/u',
                    '/^(help|tulong|tabang)$/u',
                    '/^(unsa\s+imong\s+mahimo)/u',
                ],
                'You can ask me about barangay services and requirements, published announcements and advisories, and emergency hotlines. I answer only from approved barangay sources, so anything outside those records I will refer to the barangay office.',
            ],
        ];
    }
}
