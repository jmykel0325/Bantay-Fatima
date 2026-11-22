<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Advisory;
use App\Models\EmergencyHotline;
use Illuminate\Http\JsonResponse;

class PublicContentController extends Controller
{
    public function announcements(): JsonResponse
    {
        $items = Advisory::visibleToResidents()->latest('published_at')->get();
        return response()->json(['success' => true, 'message' => 'Public updates retrieved.', 'data' => $items]);
    }

    public function announcement(Advisory $announcement): JsonResponse
    {
        abort_unless(Advisory::visibleToResidents()->whereKey($announcement->getKey())->exists(), 404);
        return response()->json(['success' => true, 'message' => 'Public update retrieved.', 'data' => $announcement]);
    }

    public function emergency(): JsonResponse
    {
        $items = EmergencyHotline::where('is_active', true)->where('status', 'active')->where('show_in_resident_app', true)->orderByRaw("CASE priority WHEN 'high' THEN 1 WHEN 'normal' THEN 2 ELSE 3 END")->orderBy('group')->orderBy('organization')->get(['id','group as service_category','organization','hotline as primary_hotline','alternative_number','hours as availability','coverage_area','address as office_address','icon_type as icon','description','priority']);
        return response()->json(['success' => true, 'message' => 'Emergency information retrieved.', 'data' => $items]);
    }

    public function about(): JsonResponse
    {
        return response()->json(['success' => true, 'data' => [
            'title' => 'About Bantay Fatima',
            'description' => 'Bantay Fatima connects residents and authorized barangay personnel for responsible community reporting and timely public information.',
            'development_notice' => 'Features continue to be improved in coordination with Barangay Fatima.',
        ]]);
    }

    public function support(): JsonResponse
    {
        return response()->json(['success' => true, 'data' => ['message' => 'For account support, contact the Barangay Fatima office.']]);
    }

    public function legal(string $document): JsonResponse
    {
        abort_unless(in_array($document, ['terms', 'privacy'], true), 404);
        return response()->json(['success' => true, 'data' => ['document' => $document, 'web_url' => url($document === 'terms' ? '/terms-and-conditions' : '/privacy-policy')]]);
    }
}
