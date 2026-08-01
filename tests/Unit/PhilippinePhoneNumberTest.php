<?php

namespace Tests\Unit;

use App\Support\PhilippinePhoneNumber;
use InvalidArgumentException;
use PHPUnit\Framework\Attributes\DataProvider;
use PHPUnit\Framework\TestCase;

class PhilippinePhoneNumberTest extends TestCase
{
    #[DataProvider('validNumbers')]
    public function test_it_normalizes_philippine_mobile_numbers(string $input): void
    {
        $this->assertSame('+639171234567', PhilippinePhoneNumber::normalize($input));
    }

    public static function validNumbers(): array
    {
        return [['09171234567'], ['0917 123 4567'], ['0917-123-4567'], ['+639171234567'], ['+63 917 123 4567']];
    }

    public function test_it_rejects_invalid_numbers(): void
    {
        $this->expectException(InvalidArgumentException::class);
        PhilippinePhoneNumber::normalize('021234567');
    }
}
