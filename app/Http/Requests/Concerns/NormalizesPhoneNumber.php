<?php

namespace App\Http\Requests\Concerns;

use App\Support\PhilippinePhoneNumber;
use InvalidArgumentException;

trait NormalizesPhoneNumber
{
    protected function prepareForValidation(): void
    {
        try {
            $this->merge(['phone_number' => PhilippinePhoneNumber::normalize($this->input('phone_number'))]);
        } catch (InvalidArgumentException) {
            // Keep the submitted value so validation returns a field-level error.
        }
    }

    protected function phoneRules(): array
    {
        return ['required', 'string', 'regex:/^\+639\d{9}$/'];
    }

    public function phoneMessages(): array
    {
        return ['phone_number.regex' => 'Enter a valid Philippine mobile number, such as 0917 123 4567.'];
    }
}
