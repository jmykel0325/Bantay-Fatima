<?php
namespace App\Http\Controllers\Admin; use App\Http\Controllers\Controller;use App\Models\Report;
class AdminReportMapController extends Controller {public function __invoke(){return view('admin.reports.map',['reports'=>Report::with(['category','purok'])->whereNotNull('latitude')->whereNotNull('longitude')->latest()->limit(500)->get()]);}}
