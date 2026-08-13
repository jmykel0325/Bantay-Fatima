<?php

namespace Tests\Feature;

use Tests\TestCase;

class ApiHealthTest extends TestCase
{
    public function test_health_endpoint_is_public_and_returns_the_expected_shape(): void
    {
        $response = $this->getJson('/api/health');

        $response->assertOk()
            ->assertJson([
                'success' => true,
                'message' => 'Bantay Fatima API is connected.',
                'data' => [
                    'application' => 'Bantay Fatima',
                ],
            ])
            ->assertJsonStructure([
                'success',
                'message',
                'data' => ['application', 'environment', 'server_time'],
            ]);
    }

    public function test_health_endpoint_does_not_leak_configuration(): void
    {
        $payload = $this->getJson('/api/health')->json();

        // Only the three documented keys may be exposed.
        $this->assertSame(
            ['application', 'environment', 'server_time'],
            array_keys($payload['data']),
        );

        $body = json_encode($payload);
        foreach (['password', 'DB_', 'APP_KEY', 'secret', base_path()] as $forbidden) {
            $this->assertStringNotContainsStringIgnoringCase($forbidden, $body);
        }
    }
}
