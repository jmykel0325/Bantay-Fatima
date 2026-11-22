<?php
namespace App\Services;
use App\Contracts\ResidentKnowledgeService;
class UnavailableResidentKnowledgeService implements ResidentKnowledgeService { public function answer(string $question, ?\App\Models\User $resident = null):array{// Small talk still works when the knowledge service is switched off.
        if($small=AssistantConversation::resolve($question))return $small+['type'=>'conversation'];
        return ['available'=>false,'answer'=>'I could not find verified Barangay Fatima information about that.','type'=>'no_context','sources'=>[]];} }
