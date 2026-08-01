<?php

use App\Http\Controllers\Api\AuthController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\Resident\ResidentApiController;

Route::prefix('auth')->group(function () {
    Route::post('/login', [AuthController::class, 'login'])->middleware('throttle:5,1');
    Route::post('/register/send-code', [AuthController::class, 'sendRegistrationCode'])->middleware('throttle:otp-send');
    Route::post('/register/verify', [AuthController::class, 'verifyRegistration'])->middleware('throttle:otp-verify');
    Route::post('/forgot-password/send-code', [AuthController::class, 'sendResetCode'])->middleware('throttle:otp-send');
    Route::post('/forgot-password/reset', [AuthController::class, 'resetPassword'])->middleware('throttle:otp-verify');
    Route::post('/logout', [AuthController::class, 'logout'])->middleware('auth:sanctum');
});

Route::get('/user', fn (Request $request) => $request->user())->middleware('auth:sanctum');

Route::middleware(['auth:sanctum','role:resident'])->prefix('resident')->group(function () {
    Route::get('/dashboard',[ResidentApiController::class,'dashboard']);
    Route::get('/reports',[ResidentApiController::class,'reports']); Route::post('/reports',[ResidentApiController::class,'store'])->middleware('throttle:5,1'); Route::get('/reports/{report}',[ResidentApiController::class,'show']);
    Route::get('/report-categories',[ResidentApiController::class,'categories']); Route::get('/puroks',[ResidentApiController::class,'puroks']);
    Route::get('/updates',[ResidentApiController::class,'updates']); Route::get('/updates/{update}',[ResidentApiController::class,'update']); Route::get('/emergency-information',[ResidentApiController::class,'emergency']);
    Route::post('/assistant/query',[ResidentApiController::class,'assistant'])->middleware('throttle:10,1');
    Route::get('/notifications',[ResidentApiController::class,'notifications']); Route::post('/notifications/read-all',[ResidentApiController::class,'notificationsReadAll']); Route::post('/notifications/{notification}/read',[ResidentApiController::class,'notificationRead']);
    Route::get('/profile',[ResidentApiController::class,'profile']); Route::put('/profile',[ResidentApiController::class,'profileUpdate']); Route::put('/password',[ResidentApiController::class,'password'])->middleware('throttle:5,1');
});
