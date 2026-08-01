<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Model;
class ReportNote extends Model { protected $fillable = ['report_id', 'user_id', 'body', 'is_resident_visible']; protected function casts(): array { return ['is_resident_visible' => 'boolean']; } }
