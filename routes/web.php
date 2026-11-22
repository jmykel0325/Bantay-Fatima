<?php

use App\Http\Controllers\Admin\AdminSessionController;
use App\Http\Controllers\Admin\AdminDashboardController;
use App\Http\Controllers\Admin\AdminReportController;
use App\Http\Controllers\Admin\AdminUserController;
use App\Http\Controllers\Admin\AdminContentController;
use App\Http\Controllers\Admin\AdminKnowledgeController;
use App\Http\Controllers\Admin\AdminReportMapController;
use App\Http\Controllers\Admin\AdminNotificationController;
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
    Route::get('/reports/print', [AdminReportController::class, 'print'])->name('reports.print');
    Route::get('/reports', [AdminReportController::class, 'index'])->name('reports.index');
    Route::get('/reports/{report}', [AdminReportController::class, 'show'])->name('reports.show');
    Route::patch('/reports/{report}', [AdminReportController::class, 'update'])->name('reports.update');
    Route::post('/reports/{report}/notes', [AdminReportController::class, 'note'])->name('reports.notes');
    Route::get('/users', [AdminUserController::class, 'index'])->name('users.index');
    Route::post('/users/staff', [AdminUserController::class, 'storeStaff'])->name('users.staff.store');
    Route::patch('/users/{user}/status', [AdminUserController::class, 'status'])->name('users.status');
    Route::patch('/users/{userId}/restore', [AdminUserController::class, 'restore'])->name('users.restore');
    Route::patch('/users/{user}', [AdminUserController::class, 'update'])->name('users.update');
    Route::delete('/users/{user}', [AdminUserController::class, 'destroy'])->name('users.destroy');
    Route::get('/report-map', AdminReportMapController::class)->name('reports.map');
    Route::get('/advisories',[AdminContentController::class,'advisories'])->name('advisories.index');
    Route::post('/advisories',[AdminContentController::class,'storeAdvisory'])->name('advisories.store');
    Route::patch('/advisories/{advisory}',[AdminContentController::class,'updateAdvisory'])->name('advisories.update');
    Route::delete('/advisories/{advisory}',[AdminContentController::class,'deleteAdvisory'])->name('advisories.destroy');
    Route::get('/hotlines',[AdminContentController::class,'hotlines'])->name('hotlines.index');
    Route::get('/hotlines/create',[AdminContentController::class,'createHotline'])->name('hotlines.create');
    Route::post('/hotlines',[AdminContentController::class,'storeHotline'])->name('hotlines.store');
    Route::patch('/hotlines/{hotline}',[AdminContentController::class,'updateHotline'])->name('hotlines.update');
    Route::delete('/hotlines/{hotline}',[AdminContentController::class,'deleteHotline'])->name('hotlines.destroy');
    Route::get('/knowledge/create',[AdminKnowledgeController::class,'create'])->name('knowledge.create');
    Route::resource('/knowledge',AdminKnowledgeController::class)->only(['index','store','update','destroy']);
    Route::post('/knowledge/test-retrieval',[AdminKnowledgeController::class,'testRetrieval'])->name('knowledge.test-retrieval');
    Route::get('/notifications',[AdminNotificationController::class,'index'])->name('notifications.index');
    Route::post('/notifications/read-all',[AdminNotificationController::class,'readAll'])->name('notifications.read-all');
});
