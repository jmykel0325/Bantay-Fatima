<?php
namespace App\Http\Requests\Admin;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Validator;

class UpdateReportRequest extends FormRequest
{
    public function authorize(): bool { return $this->user()?->role === 'admin'; }

    public function rules(): array
    {
        return [
            'validation_status' => ['sometimes', 'in:validated,rejected'],
            'rejection_reason' => ['required_if:validation_status,rejected', 'nullable', 'string', 'min:10', 'max:2000'],
            'status' => ['sometimes', 'in:pending,in_progress,resolved'],
            'priority' => ['sometimes', 'in:low,normal,high,urgent'],
            'assigned_staff_id' => ['sometimes', 'nullable', 'exists:users,id'],
            'resolution_notes' => ['required_if:status,resolved', 'nullable', 'string', 'max:5000'],
            'resolution_date' => ['required_if:status,resolved', 'nullable', 'date', 'before_or_equal:now'],
        ];
    }

    public function after(): array
    {
        return [function (Validator $validator): void {
            $report = $this->route('report');
            if (! $report) return;

            if ($report->validation_status === 'rejected') {
                if ($this->hasAny(['validation_status', 'rejection_reason', 'assigned_staff_id', 'status', 'priority', 'resolution_notes', 'resolution_date'])) {
                    $validator->errors()->add('validation_status', 'A rejected report and its rejection reason can no longer be changed.');
                }
                return;
            }

            if ($report->validation_status === 'validated') return;

            if ($this->filled('assigned_staff_id')) {
                $validator->errors()->add('assigned_staff_id', 'Validate the report before assigning staff.');
            }
            if ($this->filled('status') && in_array($this->input('status'), ['in_progress', 'resolved'], true)) {
                $validator->errors()->add('status', 'Validate the report before staff can take action.');
            }
        }];
    }
}
