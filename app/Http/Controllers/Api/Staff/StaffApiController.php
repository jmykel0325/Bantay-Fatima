<?php

namespace App\Http\Controllers\Api\Staff;

use App\Http\Controllers\Controller;
use App\Models\Report;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class StaffApiController extends Controller
{
    public function dashboard(Request $request): JsonResponse
    {
        $query = Report::where('assigned_staff_id', $request->user()->id);
        $counts = (clone $query)->selectRaw('status, COUNT(*) total')->groupBy('status')->pluck('total', 'status');
        return response()->json(['success' => true, 'message' => 'Staff dashboard retrieved.', 'data' => [
            'counts' => [
                'total' => (clone $query)->count(),
                'pending' => (int) ($counts['pending'] ?? 0),
                'in_progress' => (int) ($counts['in_progress'] ?? 0),
                'awaiting_confirmation' => (int) ($counts['awaiting_confirmation'] ?? 0),
                'completed' => (int) (($counts['resolved'] ?? 0) + ($counts['completed'] ?? 0)),
            ],
            'newly_assigned' => (clone $query)->with(['category', 'purok'])->latest()->limit(5)->get(),
        ]]);
    }

    public function assigned(Request $request): JsonResponse
    {
        $reports = Report::where('assigned_staff_id', $request->user()->id)->with(['category', 'purok'])->latest()->paginate(15);
        return response()->json(['success' => true, 'message' => 'Assigned reports retrieved.', 'data' => $reports->items(), 'meta' => ['current_page' => $reports->currentPage(), 'last_page' => $reports->lastPage(), 'total' => $reports->total()]]);
    }

    public function show(Request $request, Report $report): JsonResponse
    {
        abort_unless((int) $report->assigned_staff_id === (int) $request->user()->id, 403);
        return response()->json(['success' => true, 'data' => $report->load(['category', 'purok', 'photos', 'statusHistories'])]);
    }
}
