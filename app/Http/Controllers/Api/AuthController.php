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
        if (! $user || ! in_array($user->role, ['resident', 'staff'], true) || ! $user->email_verified_at || ! Hash::check($request->string('password'), $user->password)) {
            RateLimiter::hit($key, 60);
            $message = $user?->role === 'admin'
                ? 'This account is not authorized to use the mobile application.'
                : 'The email or password is incorrect.';
            throw ValidationException::withMessages(['email' => $message]);
        }

        if ($user->status !== 'active') {
            $message = $user->status === 'suspended'
                ? 'This account has been suspended. Please contact Barangay Fatima.'
                : 'This account is currently inactive. Please contact Barangay Fatima.';
            throw ValidationException::withMessages(['email' => $message]);
        }

        RateLimiter::clear($key);
        $token = $user->createToken($request->input('device_name', 'Android device'))->plainTextToken;

        $user->forceFill(['last_login_at' => now()])->save();

        return $this->authResponse($user, $token, 'Login successful.');
    }

    public function sendRegistrationCode(RegisterRequest $request, EmailVerificationService $verification): JsonResponse
    {
        $data = $request->validated();
        $data['email'] = mb_strtolower($data['email']);
        $data['password'] = Hash::make($data['password']);
        unset($data['password_confirmation'], $data['terms'], $data['device_name']);
        $verification->issue($data['email'], 'registration', $data);

        return response()->json([
            'success' => true,
            'message' => 'We sent a six-digit verification code to '.EmailVerificationService::mask($data['email']).'.',
            'data' => ['email' => $data['email'], 'masked_email' => EmailVerificationService::mask($data['email']), 'expires_in' => 600, 'resend_after' => 60],
        ]);
    }

    public function resendRegistrationCode(Request $request, EmailVerificationService $verification): JsonResponse
    {
        $validated = $request->validate(['email' => ['required', 'email:rfc', 'ends_with:@gmail.com', 'max:255']]);
        $email = mb_strtolower(trim($validated['email']));
        $record = \App\Models\EmailVerificationCode::where('email', $email)->where('purpose', 'registration')->first();
        if (! $record) {
            throw ValidationException::withMessages(['email' => 'Start registration before requesting another verification code.']);
        }

        $verification->issue($email, 'registration', $record->payload, true);

        return response()->json([
            'success' => true,
            'message' => 'A new verification code was sent.',
            'data' => ['email' => $email, 'masked_email' => EmailVerificationService::mask($email), 'expires_in' => 600, 'resend_after' => 60],
        ]);
    }

    public function verifyRegistration(VerifyRegistrationRequest $request, EmailVerificationService $verification): JsonResponse
    {
        $record = $verification->verify($request->string('email')->lower()->toString(), 'registration', $request->string('code')->toString());
        $data = $record->payload;

        $user = DB::transaction(function () use ($data, $record): User {
            $duplicate = User::where('email', $data['email']);
            if (! empty($data['phone_number'])) {
                $duplicate->orWhere('phone_number', $data['phone_number']);
            }
            if ($duplicate->exists()) {
                throw ValidationException::withMessages(['email' => 'An account is already registered with this email.']);
            }
            $user = User::create([...$data, 'email_verified_at' => now(), 'role' => 'resident', 'status' => 'active']);
            $record->delete();

            return $user;
        });

        $token = $user->createToken($request->input('device_name', 'Android device'))->plainTextToken;

        return $this->authResponse($user, $token, 'Resident account verified and created.', 201);
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

        return response()->json(['success' => true, 'message' => 'Logged out successfully.']);
    }

    private function authResponse(User $user, string $token, string $message, int $status = 200): JsonResponse
    {
        return response()->json([
            'success' => true,
            'message' => $message,
            'data' => [
                'token' => $token,
                'token_type' => 'Bearer',
                'user' => $user->only(['id', 'first_name', 'middle_name', 'last_name', 'suffix', 'email', 'role', 'status', 'email_verified_at']),
            ],
        ], $status);
    }
}
