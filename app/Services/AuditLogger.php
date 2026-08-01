<?php

namespace App\Services;

use App\Models\AuditLog;
use Illuminate\Database\Eloquent\Model;

class AuditLogger
{
    private const SENSITIVE = ['password', 'password_confirmation', 'remember_token', 'token', 'code'];

    public function log(string $action, ?Model $entity, string $description, array $old = [], array $new = []): AuditLog
    {
        return AuditLog::create([
            'user_id' => auth()->id(), 'action' => $action,
            'entity_type' => $entity ? $entity::class : null, 'entity_id' => $entity?->getKey(),
            'description' => $description,
            'old_values' => $this->safe($old), 'new_values' => $this->safe($new),
            'ip_address' => request()->ip(), 'user_agent' => mb_substr((string) request()->userAgent(), 0, 1000),
        ]);
    }

    private function safe(array $values): array
    {
        return collect($values)->except(self::SENSITIVE)->all();
    }
}
