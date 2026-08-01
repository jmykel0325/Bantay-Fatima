<?php

namespace App\Services;

use App\Mail\VerificationCodeMail;
use App\Models\EmailVerificationCode;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Illuminate\Validation\ValidationException;
use Throwable;

class EmailVerificationService
{
    public function issue(string $email, string $purpose, array $payload = []): void
    {
        $code = str_pad((string) random_int(0, 999999), 6, '0', STR_PAD_LEFT);

        $record = EmailVerificationCode::updateOrCreate(
            ['email' => mb_strtolower($email), 'purpose' => $purpose],
            ['code_hash' => Hash::make($code), 'payload' => $payload, 'attempts' => 0, 'expires_at' => now()->addMinutes(10)],
        );

        try {
            Mail::to($email)->send(new VerificationCodeMail($code, $purpose));
        } catch (Throwable $exception) {
            $record->delete();
            report($exception);
            throw ValidationException::withMessages(['email' => 'We could not send the verification email. Check the mail configuration and try again.']);
        }
    }

    public function verify(string $email, string $purpose, string $code): EmailVerificationCode
    {
        $record = EmailVerificationCode::where('email', mb_strtolower($email))->where('purpose', $purpose)->first();

        if (! $record || $record->expires_at->isPast()) {
            $record?->delete();
            throw ValidationException::withMessages(['code' => 'The verification code is invalid or expired. Request a new code.']);
        }

        if ($record->attempts >= 5) {
            throw ValidationException::withMessages(['code' => 'Too many incorrect attempts. Request a new code.']);
        }

        if (! Hash::check($code, $record->code_hash)) {
            $record->increment('attempts');
            throw ValidationException::withMessages(['code' => 'The verification code is incorrect.']);
        }

        return $record;
    }

    public static function mask(string $email): string
    {
        [$name, $domain] = explode('@', $email, 2);
        $visible = mb_substr($name, 0, min(2, mb_strlen($name)));

        return $visible.str_repeat('•', max(3, mb_strlen($name) - 2)).'@'.$domain;
    }
}
