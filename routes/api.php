<?php

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\HealthController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\Resident\ResidentApiController;
use App\Http\Controllers\Api\PublicContentController;
use App\Http\Controllers\Api\Staff\StaffApiController;

// Unauthenticated connectivity probe used by the Android application.
Route::get('/health', HealthController::class)->name('api.health');

Route::prefix('public')->group(function () {
    Route::get('/announcements', [PublicContentController::class, 'announcements']);
    Route::get('/announcements/{announcement}', [PublicContentController::class, 'announcement']);
    Route::get('/emergency-information', [PublicContentController::class, 'emergency']);
    Route::get('/about', [PublicContentController::class, 'about']);
    Route::get('/support', [PublicContentController::class, 'support']);
    Route::get('/legal/{document}', [PublicContentController::class, 'legal'])->whereIn('document', ['terms', 'privacy']);
});

Route::prefix('auth')->group(function () {
    Route::post('/login', [AuthController::class, 'login'])->middleware('throttle:5,1');
    Route::post('/register/send-code', [AuthController::class, 'sendRegistrationCode'])->middleware('throttle:otp-send');
    Route::post('/register/request-code', [AuthController::class, 'sendRegistrationCode'])->middleware('throttle:otp-send');
    Route::post('/register/verify', [AuthController::class, 'verifyRegistration'])->middleware('throttle:otp-verify');
    Route::post('/register/verify-code', [AuthController::class, 'verifyRegistration'])->middleware('throttle:otp-verify');
    Route::post('/register/resend-code', [AuthController::class, 'resendRegistrationCode'])->middleware('throttle:otp-send');
    Route::post('/forgot-password/send-code', [AuthController::class, 'sendResetCode'])->middleware('throttle:otp-send');
    Route::post('/forgot-password/reset', [AuthController::class, 'resetPassword'])->middleware('throttle:otp-verify');
    Route::post('/logout', [AuthController::class, 'logout'])->middleware('auth:sanctum');
});

Route::get('/user', fn (Request $request) => response()->json(['success' => true, 'data' => $request->user()->only(['id','first_name','middle_name','last_name','suffix','email','role','status','email_verified_at'])]))->middleware('auth:sanctum');

Route::middleware(['auth:sanctum','role:resident'])->prefix('resident')->group(function () {
    Route::get('/dashboard',[ResidentApiController::class,'dashboard']);
    Route::get('/reports',[ResidentApiController::class,'reports']); Route::post('/reports',[ResidentApiController::class,'store'])->middleware('throttle:5,1'); Route::get('/reports/{report}',[ResidentApiController::class,'show']);
    Route::get('/report-categories',[ResidentApiController::class,'categories']); Route::get('/puroks',[ResidentApiController::class,'puroks']);
    Route::get('/updates',[ResidentApiController::class,'updates']); Route::get('/updates/{update}',[ResidentApiController::class,'update']); Route::get('/emergency-information',[ResidentApiController::class,'emergency']);
    Route::post('/assistant/query',[ResidentApiController::class,'assistant'])->middleware('throttle:10,1');
    Route::get('/notifications',[ResidentApiController::class,'notifications']); Route::post('/notifications/read-all',[ResidentApiController::class,'notificationsReadAll']); Route::post('/notifications/{notification}/read',[ResidentApiController::class,'notificationRead']);
    Route::get('/profile',[ResidentApiController::class,'profile']); Route::put('/profile',[ResidentApiController::class,'profileUpdate']); Route::put('/password',[ResidentApiController::class,'password'])->middleware('throttle:5,1');
});

Route::middleware(['auth:sanctum', 'role:staff'])->prefix('staff')->group(function () {
    Route::get('/dashboard', [StaffApiController::class, 'dashboard']);
    Route::get('/assigned-reports', [StaffApiController::class, 'assigned']);
    Route::get('/assigned-reports/{report}', [StaffApiController::class, 'show']);
});
