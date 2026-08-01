<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\SoftDeletes;

class Report extends Model
{
    use SoftDeletes;

    protected $fillable = ['reference_number', 'resident_id', 'category_id', 'purok_id', 'assigned_staff_id', 'title', 'description', 'priority', 'status', 'validation_status', 'rejection_reason', 'validated_at', 'latitude', 'longitude', 'location_notes', 'submission_token', 'cancellation_reason', 'cancellation_requested_at', 'resolved_at', 'resolution_notes', 'resolution_photo_path'];
    protected function casts(): array { return ['resolved_at' => 'datetime', 'validated_at' => 'datetime', 'cancellation_requested_at' => 'datetime', 'latitude' => 'decimal:7', 'longitude' => 'decimal:7']; }
    public function resident(): BelongsTo { return $this->belongsTo(User::class, 'resident_id'); }
    public function assignedStaff(): BelongsTo { return $this->belongsTo(User::class, 'assigned_staff_id'); }
    public function category(): BelongsTo { return $this->belongsTo(ReportCategory::class, 'category_id'); }
    public function purok(): BelongsTo { return $this->belongsTo(Purok::class); }
    public function photos(): HasMany { return $this->hasMany(ReportPhoto::class); }
    public function statusHistories(): HasMany { return $this->hasMany(ReportStatusHistory::class); }
    public function assignments(): HasMany { return $this->hasMany(ReportAssignment::class); }
    public function notes(): HasMany { return $this->hasMany(ReportNote::class); }
}
