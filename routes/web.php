<?php

use App\Http\Controllers\Admin\AdminSessionController;
use App\Http\Controllers\Admin\AdminDashboardController;
use App\Http\Controllers\Admin\AdminReportController;
use App\Http\Controllers\Admin\AdminUserController;
use App\Http\Controllers\ReportPhotoController;
use Illuminate\Support\Facades\Route;

Route::view('/', 'welcome')->name('home');

Route::view('/terms-and-conditions', 'legal.terms')->name('terms');
Route::view('/privacy-policy', 'legal.privacy')->name('privacy');

Route::get('/admin/login', [AdminSessionController::class, 'create'])->name('admin.login');
Route::post('/admin/login', [AdminSessionController::class, 'store'])->middleware('throttle:5,1')->name('admin.login.store');

Route::middleware(['auth', 'role:admin'])->prefix('admin')->name('admin.')->group(function () {
    Route::post('/logout', [AdminSessionController::class, 'destroy'])->name('logout');
    Route::get('/report-photos/{photo}', ReportPhotoController::class)->name('report-photos.show');
    Route::get('/report-media/{path}', [ReportPhotoController::class, 'showPath'])->where('path', '.*')->name('report-media.show');
    Route::get('/dashboard', AdminDashboardController::class)->name('dashboard');
    Route::get('/reports/export', [AdminReportController::class, 'export'])->name('reports.export');
    Route::get('/reports', [AdminReportController::class, 'index'])->name('reports.index');
    Route::get('/reports/{report}', [AdminReportController::class, 'show'])->name('reports.show');
    Route::patch('/reports/{report}', [AdminReportController::class, 'update'])->name('reports.update');
    Route::post('/reports/{report}/notes', [AdminReportController::class, 'note'])->name('reports.notes');
    Route::get('/users', [AdminUserController::class, 'index'])->name('users.index');
    Route::post('/users/staff', [AdminUserController::class, 'storeStaff'])->name('users.staff.store');
    Route::patch('/users/{user}/status', [AdminUserController::class, 'status'])->name('users.status');
});
