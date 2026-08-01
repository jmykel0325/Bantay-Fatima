<?php
namespace App\Http\Requests\Admin;
use App\Http\Requests\Concerns\NormalizesPhoneNumber;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;
use Illuminate\Validation\Rules\Password;
class StoreStaffRequest extends FormRequest { use NormalizesPhoneNumber; public function authorize(): bool { return $this->user()?->role === 'admin'; } public function rules(): array { return ['first_name'=>['required','string','max:80'],'middle_name'=>['nullable','string','max:80'],'last_name'=>['required','string','max:80'],'suffix'=>['nullable','string','max:20'],'email'=>['required','email:rfc','max:255',Rule::unique('users')],'phone_number'=>[...$this->phoneRules(),Rule::unique('users')],'password'=>['required','confirmed',Password::min(10)->letters()->numbers()],'status'=>['required','in:active,inactive'],'position'=>['nullable','string','max:100'],'department'=>['nullable','string','max:100']]; } }
