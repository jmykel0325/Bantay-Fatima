<?php
namespace App\Http\Controllers\Admin;use App\Http\Controllers\Controller;use Illuminate\Http\Request;
class AdminNotificationController extends Controller {public function index(Request $r){return view('admin.notifications.index',['notifications'=>$r->user()->notifications()->latest()->paginate(20)]);}public function readAll(Request $r){$r->user()->unreadNotifications()->update(['read_at'=>now()]);return back()->with('success','Notifications marked as read.');}}
