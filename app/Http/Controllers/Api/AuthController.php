<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\ForgotEmailRequest;
use App\Http\Requests\LoginRequest;
use App\Http\Requests\RegisterRequest;
use App\Http\Requests\ResetPasswordRequest;
use App\Http\Requests\VerifyRegistrationRequest;
use App\Models\User;
use App\Services\EmailVerificationService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\RateLimiter;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function login(LoginRequest $request): JsonResponse
    {
        $email = mb_strtolower($request->string('email')->toString());
        $key = 'api-login:'.hash('sha256', $email.'|'.$request->ip());

        if (RateLimiter::tooManyAttempts($key, 5)) {
            throw ValidationException::withMessages(['email' => 'Too many login attempts. Try again in '.RateLimiter::availableIn($key).' seconds.']);
        }

        $user = User::where('email', $email)->first();
        if (! $user || ! in_array($user->role, ['resident', 'staff'], true) || ! $user->email_verified_at || ! Hash::check($request->string('password'), $user->password) || $user->status !== 'active') {
            RateLimiter::hit($key, 60);
            throw ValidationException::withMessages(['email' => 'The provided email address or password is incorrect.']);
        }

        RateLimiter::clear($key);
        $token = $user->createToken($request->input('device_name', 'Android device'))->plainTextToken;

        return response()->json(['success' => true, 'message' => 'Login successful.', 'token' => $token, 'token_type' => 'Bearer', 'user' => $user->only(['id','first_name','last_name','role','status'])]);
    }

    public function sendRegistrationCode(RegisterRequest $request, EmailVerificationService $verification): JsonResponse
    {
        $data = $request->validated();
        $data['email'] = mb_strtolower($data['email']);
        $data['password'] = Hash::make($data['password']);
        unset($data['password_confirmation'], $data['terms'], $data['device_name']);
        $verification->issue($data['email'], 'registration', $data);

        return response()->json(['message' => 'Verification code sent.', 'email' => $data['email']]);
    }

    public function verifyRegistration(VerifyRegistrationRequest $request, EmailVerificationService $verification): JsonResponse
    {
        $record = $verification->verify($request->string('email')->lower()->toString(), 'registration', $request->string('code')->toString());
        $data = $record->payload;

        $user = DB::transaction(function () use ($data, $record): User {
            if (User::where('email', $data['email'])->orWhere('phone_number', $data['phone_number'])->exists()) {
                throw ValidationException::withMessages(['email' => 'An account already exists with this email address or phone number.']);
            }
            $user = User::create([...$data, 'email_verified_at' => now(), 'role' => 'resident', 'status' => 'active']);
            $record->delete();

            return $user;
        });

        $token = $user->createToken($request->input('device_name', 'Android device'))->plainTextToken;

        return response()->json(['user' => $user, 'token' => $token, 'token_type' => 'Bearer'], 201);
    }

    public function sendResetCode(ForgotEmailRequest $request, EmailVerificationService $verification): JsonResponse
    {
        $email = mb_strtolower($request->string('email')->toString());
        if ($user = User::where('email', $email)->whereNotNull('email_verified_at')->first()) {
            $verification->issue($email, 'password_reset', ['user_id' => $user->id]);
        }

        return response()->json(['message' => 'If the email belongs to a verified account, a code has been sent.']);
    }

    public function resetPassword(ResetPasswordRequest $request, EmailVerificationService $verification): JsonResponse
    {
        $record = $verification->verify(mb_strtolower($request->string('email')->toString()), 'password_reset', $request->string('code')->toString());
        DB::transaction(function () use ($record, $request): void {
            $user = User::lockForUpdate()->findOrFail($record->payload['user_id']);
            $user->forceFill(['password' => Hash::make($request->string('password')->toString())])->save();
            $user->tokens()->delete();
            $record->delete();
        });

        return response()->json(['message' => 'Password updated successfully.']);
    }

    public function logout(Request $request): JsonResponse
    {
        $request->user()->currentAccessToken()?->delete();

        return response()->json(['message' => 'Logged out successfully.']);
    }
}
