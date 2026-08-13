<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\JsonResponse;

/**
 * Public connectivity probe for the Bantay Fatima mobile application.
 *
 * Intentionally returns no configuration, credentials, paths or version
 * details. It only confirms that the API layer is reachable and responding.
 */
class HealthController extends Controller
{
    public function __invoke(): JsonResponse
    {
        return response()->json([
            'success' => true,
            'message' => 'Bantay Fatima API is connected.',
            'data' => [
                'application' => 'Bantay Fatima',
                'environment' => app()->environment(),
                'server_time' => now()->toIso8601String(),
            ],
        ]);
    }
}
