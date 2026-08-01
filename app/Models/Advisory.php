<?php
namespace App\Models;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
class Advisory extends Model { use SoftDeletes; protected $guarded=[]; protected function casts():array{return ['is_urgent'=>'boolean','published_at'=>'datetime','expires_at'=>'datetime'];} public function scopeVisibleToResidents($q){return $q->where('status','published')->whereIn('audience',['residents','everyone'])->where('published_at','<=',now())->where(fn($s)=>$s->whereNull('expires_at')->orWhere('expires_at','>',now()));} }
