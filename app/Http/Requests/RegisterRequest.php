<?php

namespace App\Http\Requests;

use App\Http\Requests\Concerns\NormalizesPhoneNumber;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;
use Illuminate\Validation\Rules\Password;

class RegisterRequest extends FormRequest
{
    use NormalizesPhoneNumber {
        prepareForValidation as normalizePhoneNumber;
    }

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
            'email' => ['required', 'email:rfc', 'max:255', 'ends_with:@gmail.com', Rule::unique('users', 'email')],
            'phone_number' => [...$this->phoneRules(), Rule::unique('users', 'phone_number')],
            'password' => ['required', 'confirmed', Password::min(8)->mixedCase()->numbers()],
            'terms' => ['accepted'],
        ];
    }

    public function messages(): array
    {
        return [
            ...$this->phoneMessages(),
            'phone_number.required' => 'Please enter your Philippine mobile number.',
            'phone_number.unique' => 'An account is already registered with this phone number.',
            'email.ends_with' => 'Please use a valid Gmail address ending in @gmail.com.',
            'email.unique' => 'An account is already registered with this email.',
            'first_name.regex' => 'Use letters, spaces, hyphens, or apostrophes only.',
            'middle_name.regex' => 'Use letters, spaces, hyphens, or apostrophes only.',
            'last_name.regex' => 'Use letters, spaces, hyphens, or apostrophes only.',
            'suffix.regex' => 'Use letters and periods only for the suffix.',
            'terms.accepted' => 'You must agree to the Terms and Conditions and Privacy Policy.',
        ];
    }

    protected function prepareForValidation(): void
    {
        $this->normalizePhoneNumber();
        $this->merge([
            'email' => mb_strtolower(trim((string) $this->input('email'))),
            'middle_name' => $this->filled('middle_name') ? trim((string) $this->input('middle_name')) : null,
            'suffix' => $this->filled('suffix') ? trim((string) $this->input('suffix')) : null,
        ]);
    }
}
