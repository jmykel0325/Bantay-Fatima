<?php

namespace App\Http\Requests;

use App\Http\Requests\Concerns\NormalizesPhoneNumber;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;
use Illuminate\Validation\Rules\Password;

class RegisterRequest extends FormRequest
{
    use NormalizesPhoneNumber;

    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        $nameRules = ['required', 'string', 'max:80', 'regex:/^[\p{L}\p{M}]+(?:[\s\'\-][\p{L}\p{M}]+)*$/u'];

        return [
            'first_name' => $nameRules,
            'middle_name' => ['nullable', 'string', 'max:80', 'regex:/^[\p{L}\p{M}]+(?:[\s\'\-][\p{L}\p{M}]+)*$/u'],
            'last_name' => $nameRules,
            'suffix' => ['nullable', 'string', 'max:20', 'regex:/^[\p{L}\p{M}.]+$/u'],
            'email' => ['required', 'email:rfc', 'max:255', Rule::unique('users', 'email')],
            'phone_number' => [...$this->phoneRules(), Rule::unique('users', 'phone_number')],
            'password' => ['required', 'confirmed', Password::min(8)->letters()->numbers()],
            'terms' => ['accepted'],
        ];
    }

    public function messages(): array
    {
        return [
            ...$this->phoneMessages(),
            'phone_number.unique' => 'An account is already registered with this phone number.',
            'email.unique' => 'An account is already registered with this email address.',
            'first_name.regex' => 'Use letters, spaces, hyphens, or apostrophes only.',
            'middle_name.regex' => 'Use letters, spaces, hyphens, or apostrophes only.',
            'last_name.regex' => 'Use letters, spaces, hyphens, or apostrophes only.',
            'suffix.regex' => 'Use letters and periods only for the suffix.',
            'terms.accepted' => 'You must agree to the Terms and Conditions and Privacy Policy.',
        ];
    }
}
