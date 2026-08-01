<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Model;
class ReportPhoto extends Model { protected $fillable = ['report_id', 'path', 'original_name', 'mime_type', 'size']; public function report(){return $this->belongsTo(Report::class);} }
