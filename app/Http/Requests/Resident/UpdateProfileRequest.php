<?php
namespace App\Http\Requests\Resident;
use Illuminate\Foundation\Http\FormRequest;use Illuminate\Validation\Rule;
class UpdateProfileRequest extends FormRequest { public function authorize():bool{return $this->user()?->role==='resident';} public function rules():array{return ['first_name'=>['required','string','max:80'],'middle_name'=>['nullable','string','max:80'],'last_name'=>['required','string','max:80'],'suffix'=>['nullable','string','max:20'],'email'=>['required','email:rfc','max:255',Rule::unique('users')->ignore($this->user())],'purok_id'=>['nullable',Rule::exists('puroks','id')->where('is_active',true)],'address'=>['nullable','string','max:255']];} }
