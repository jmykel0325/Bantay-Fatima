<?php
namespace App\Models; use Illuminate\Database\Eloquent\Model;
class KnowledgeChunk extends Model {
    protected $fillable = ['knowledge_document_id','position','content','search_text','embedding','embedding_model','embedding_dimensions','metadata'];
    protected function casts(): array { return ['embedding' => 'array', 'metadata' => 'array']; }
    public function document(){return $this->belongsTo(KnowledgeDocument::class,'knowledge_document_id');}
}
