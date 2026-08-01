<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Model;
class ReportAssignment extends Model { public $timestamps = false; protected $fillable = ['report_id', 'staff_id', 'assigned_by', 'assigned_at', 'unassigned_at']; protected function casts(): array { return ['assigned_at' => 'datetime', 'unassigned_at' => 'datetime']; } }
