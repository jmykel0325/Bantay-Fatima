<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
class EmergencyHotline extends Model { use SoftDeletes; protected $guarded=[]; protected function casts():array{return ['is_active'=>'boolean','show_in_resident_app'=>'boolean','last_verified_at'=>'datetime'];} }
