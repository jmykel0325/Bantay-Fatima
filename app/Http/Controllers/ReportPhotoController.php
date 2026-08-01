<?php

namespace App\Http\Controllers;

use App\Models\ReportPhoto;
use Illuminate\Support\Facades\Storage;
use Symfony\Component\HttpFoundation\StreamedResponse;

class ReportPhotoController extends Controller
{
    public function __invoke(ReportPhoto $photo): StreamedResponse
    {
        $photo->loadMissing('report');
        $this->authorize('view', $photo->report);

        return $this->respond($photo);
    }

    public function showPath(string $path): StreamedResponse
    {
        $photo = ReportPhoto::with('report')->where('path', $path)->firstOrFail();
        $this->authorize('view', $photo->report);

        return $this->respond($photo);
    }

    private function respond(ReportPhoto $photo): StreamedResponse
    {
        abort_unless(Storage::disk('public')->exists($photo->path), 404);

        return Storage::disk('public')->response(
            $photo->path,
            $photo->original_name ?: basename($photo->path),
            [
                'Content-Type' => $photo->mime_type ?: Storage::disk('public')->mimeType($photo->path),
                'Cache-Control' => 'private, max-age=300',
                'X-Content-Type-Options' => 'nosniff',
            ],
        );
    }
}
