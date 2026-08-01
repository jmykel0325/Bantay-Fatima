<?php
namespace App\Services;
use App\Contracts\ResidentKnowledgeService;
class UnavailableResidentKnowledgeService implements ResidentKnowledgeService { public function answer(string $question):array{return ['available'=>false,'answer'=>'I could not find an approved Barangay Fatima source that answers this question. Please contact the barangay office for confirmation.','sources'=>[]];} }
