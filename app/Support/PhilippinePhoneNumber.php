<?php

namespace App\Support;

use InvalidArgumentException;

final class PhilippinePhoneNumber
{
    public static function normalize(?string $value): string
    {
        $phone = preg_replace('/[\s\-()]+/', '', trim((string) $value));

        if (preg_match('/^09\d{9}$/', $phone)) {
            return '+63'.substr($phone, 1);
        }

        if (preg_match('/^\+639\d{9}$/', $phone)) {
            return $phone;
        }

        throw new InvalidArgumentException('Enter a valid Philippine mobile number, such as 0917 123 4567.');
    }

    public static function isValid(?string $value): bool
    {
        try {
            self::normalize($value);

            return true;
        } catch (InvalidArgumentException) {
            return false;
        }
    }

    public static function mask(string $normalized): string
    {
        return substr($normalized, 0, 4).' ••• ••• '.substr($normalized, -4);
    }
}
