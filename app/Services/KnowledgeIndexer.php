<?php
namespace App\Services;
use App\Models\KnowledgeDocument;
use App\Services\Rag\OpenRouterService;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;
class KnowledgeIndexer {
 public function __construct(private readonly OpenRouterService $openRouter) {}
 public function index(KnowledgeDocument $document):void {
  $plain=trim(preg_replace('/\s+/u',' ',strip_tags($document->content)));$parts=[];
  for($offset=0,$length=mb_strlen($plain);$offset<$length;$offset+=900)$parts[]=mb_substr($plain,$offset,1200);
  if(!$this->openRouter->configured()){Log::warning('Knowledge was not indexed: OpenRouter is not configured.',['document_id'=>$document->id]);$document->update(['is_indexed'=>false]);return;}
  $embeddings=$this->openRouter->embedMany($parts);if(count($embeddings)!==count($parts)){Log::warning('Knowledge indexing failed.',['document_id'=>$document->id]);$document->update(['is_indexed'=>false]);return;}
  $indexed=[];foreach($parts as $position=>$content){$indexed[]=['position'=>$position,'content'=>$content,'embedding'=>$embeddings[$position]];}
  DB::transaction(function() use ($document,$indexed) {$document->newQuery()->whereKey($document->id)->lockForUpdate()->first();$document->chunks()->delete();foreach($indexed as $chunk)$document->chunks()->create(['position'=>$chunk['position'],'content'=>$chunk['content'],'search_text'=>mb_strtolower($chunk['content']),'embedding'=>$chunk['embedding'],'embedding_model'=>config('services.openrouter.embedding_model'),'embedding_dimensions'=>count($chunk['embedding']),'metadata'=>['indexed_at'=>now()->toIso8601String()]]);$document->update(['is_indexed'=>true]);});
 }
}
