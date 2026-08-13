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
    public function issue(string $email, string $purpose, array $payload = [], bool $resend = false): EmailVerificationCode
    {
        $email = mb_strtolower(trim($email));
        $existing = EmailVerificationCode::where('email', $email)->where('purpose', $purpose)->first();
        if ($resend && $existing?->last_sent_at && $existing->last_sent_at->gt(now()->subSeconds(60))) {
            throw ValidationException::withMessages(['email' => 'Please wait before requesting another verification code.']);
        }

        $code = str_pad((string) random_int(0, 999999), 6, '0', STR_PAD_LEFT);

        $record = EmailVerificationCode::updateOrCreate(
            ['email' => $email, 'purpose' => $purpose],
            ['code_hash' => Hash::make($code), 'payload' => $payload, 'attempts' => 0, 'expires_at' => now()->addMinutes(10), 'last_sent_at' => now(), 'verified_at' => null],
        );

        try {
            Mail::to($email)->send(new VerificationCodeMail($code, $purpose));
        } catch (Throwable $exception) {
            $record->delete();
            report($exception);
            throw ValidationException::withMessages(['email' => 'We could not send the verification email. Check the mail configuration and try again.']);
        }

        return $record;
    }

    public function verify(string $email, string $purpose, string $code): EmailVerificationCode
    {
        $record = EmailVerificationCode::where('email', mb_strtolower($email))->where('purpose', $purpose)->first();

        if (! $record || $record->expires_at->isPast()) {
            $record?->delete();
            throw ValidationException::withMessages(['code' => 'This verification code has expired. Request a new code.']);
        }

        if ($record->attempts >= 5) {
            throw ValidationException::withMessages(['code' => 'Too many verification attempts. Please request a new code.']);
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

        return $visible.str_repeat('*', max(3, mb_strlen($name) - 2)).'@'.$domain;
    }
}
