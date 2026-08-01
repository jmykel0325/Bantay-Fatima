<?php
namespace App\Notifications;
use App\Models\Report;
use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;
class ReportActivityNotification extends Notification { use Queueable; public function __construct(public Report $report,public string $title,public string $message){} public function via(object $notifiable):array{return ['database'];} public function toArray(object $notifiable):array{return ['title'=>$this->title,'message'=>$this->message,'report_id'=>$this->report->id,'reference_number'=>$this->report->reference_number,'url'=>'/api/resident/reports/'.$this->report->id,'deep_link'=>'bantayfatima://reports/'.$this->report->id];} }
