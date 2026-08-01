<?php
namespace App\Policies;
use App\Models\Report;
use App\Models\User;
class ReportPolicy { public function view(User $user, Report $report):bool{return $user->role==='admin'||($user->role==='resident'&&$report->resident_id===$user->id)||($user->role==='staff'&&$report->assigned_staff_id===$user->id);} public function update(User $user,Report $report):bool{return $user->role==='resident'&&$report->resident_id===$user->id&&$report->validation_status==='unverified'&&$report->status==='pending';} }
