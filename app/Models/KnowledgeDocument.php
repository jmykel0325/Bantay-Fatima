<?php
namespace App\Models; use Illuminate\Database\Eloquent\Model; use Illuminate\Database\Eloquent\SoftDeletes;
class KnowledgeDocument extends Model {use SoftDeletes; protected $fillable=['title','source_type','original_name','file_path','content','is_approved','is_indexed','created_by','approved_at']; protected function casts():array{return ['is_approved'=>'boolean','is_indexed'=>'boolean','approved_at'=>'datetime'];} public function chunks(){return $this->hasMany(KnowledgeChunk::class);} }
