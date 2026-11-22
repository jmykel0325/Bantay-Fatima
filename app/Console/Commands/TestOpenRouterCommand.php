<?php

namespace App\Console\Commands;

use App\Services\Rag\OpenRouterService;
use Illuminate\Console\Command;

class TestOpenRouterCommand extends Command
{
    protected $signature = 'bantay:test-openrouter';
    protected $description = 'Safely verifies that the configured OpenRouter model can return a completion.';

    public function handle(OpenRouterService $openRouter): int
    {
        if (! $openRouter->configured()) {
            $this->error('OPENROUTER_API_KEY is not configured.');
            return self::FAILURE;
        }

        $answer = $openRouter->chat('Reply with the word connected.', 'SOURCE: Connectivity test\nThe assistant is running a development connectivity test.');
        if (! $answer) {
            $this->error('OpenRouter did not return a valid completion. Check Laravel logs for the HTTP status.');
            return self::FAILURE;
        }

        $this->info('OpenRouter connection succeeded.');
        return self::SUCCESS;
    }
}
