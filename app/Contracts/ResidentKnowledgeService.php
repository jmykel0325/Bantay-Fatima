<?php
namespace App\Contracts;
use App\Models\User;
interface ResidentKnowledgeService { public function answer(string $question, ?User $resident = null): array; }
