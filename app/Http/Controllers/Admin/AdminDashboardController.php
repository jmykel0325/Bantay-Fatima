<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\AuditLog;
use App\Models\Report;
use App\Models\User;
use Illuminate\View\View;

class AdminDashboardController extends Controller
{
    public function __invoke(): View
    {
        $total = Report::count();
        $statusCounts = Report::selectRaw('status, COUNT(*) total')->groupBy('status')->pluck('total', 'status');
        $resolvedReports = Report::whereNotNull('resolved_at')->get(['created_at', 'resolved_at']);
        $averageHours = $resolvedReports->isEmpty() ? null : $resolvedReports->avg(fn (Report $report) => $report->created_at->diffInMinutes($report->resolved_at) / 60);
        $stats = [
            'total' => $total, 'pending' => (int) ($statusCounts['pending'] ?? 0),
            'in_progress' => (int) ($statusCounts['in_progress'] ?? 0), 'resolved' => (int) ($statusCounts['resolved'] ?? 0),
            'residents' => User::where('role', 'resident')->count(), 'staff' => User::where('role', 'staff')->where('status', 'active')->count(),
            'notifications' => 0, 'average_resolution' => $averageHours ? round($averageHours, 1).' hrs' : '—',
        ];
        $monthly = Report::where('created_at', '>=', now()->subMonths(11)->startOfMonth())->get(['created_at'])->countBy(fn (Report $report) => $report->created_at->format('Y-m'))->sortKeys();
        $byCategory = Report::join('report_categories', 'reports.category_id', '=', 'report_categories.id')->selectRaw('report_categories.name label, COUNT(*) total')->groupBy('report_categories.id', 'report_categories.name')->pluck('total', 'label');

        return view('admin.dashboard', compact('stats', 'monthly', 'byCategory') + [
            'recentReports' => Report::with(['resident', 'category', 'purok', 'assignedStaff'])->latest()->limit(8)->get(),
            'activities' => AuditLog::with('user')->latest()->limit(8)->get(),
        ]);
    }
}
