<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Model;
class ReportStatusHistory extends Model { public $timestamps = false; protected $fillable = ['report_id', 'changed_by', 'from_status', 'to_status', 'notes', 'created_at']; protected function casts(): array { return ['created_at' => 'datetime']; } }
