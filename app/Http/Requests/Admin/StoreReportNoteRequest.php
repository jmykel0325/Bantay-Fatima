<?php
namespace App\Http\Requests\Admin;
use Illuminate\Foundation\Http\FormRequest;
class StoreReportNoteRequest extends FormRequest { public function authorize(): bool { return $this->user()?->role === 'admin'; } public function rules(): array { return ['body' => ['required','string','max:5000'], 'is_resident_visible' => ['sometimes','boolean']]; } }
