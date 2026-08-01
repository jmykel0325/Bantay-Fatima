<?php
namespace App\Http\Controllers\Admin;
use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreStaffRequest;
use App\Models\User;
use App\Services\AuditLogger;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\View\View;

class AdminUserController extends Controller
{
    public function index(Request $request): View { $users=User::withTrashed()->withCount(['residentReports','assignedReports'])->when($request->q,fn($q,$v)=>$q->where(fn($s)=>$s->where('first_name','like',"%$v%")->orWhere('last_name','like',"%$v%")->orWhere('email','like',"%$v%")->orWhere('phone_number','like',"%$v%")))->when($request->role,fn($q,$v)=>$q->where('role',$v))->when($request->status,fn($q,$v)=>$q->where('status',$v))->orderBy('last_name')->paginate(15)->withQueryString(); return view('admin.users.index',compact('users')); }
    public function storeStaff(StoreStaffRequest $request, AuditLogger $audit) { $user=DB::transaction(function()use($request,$audit){$data=$request->validated();unset($data['password_confirmation']);$user=User::create([...$data,'email'=>mb_strtolower($data['email']),'email_verified_at'=>now(),'role'=>'staff','must_change_password'=>true,'created_by'=>auth()->id()]);$audit->log('staff.created',$user,'Staff account created.',[],['email'=>$user->email,'role'=>'staff','status'=>$user->status]);return $user;}); return back()->with('success',"Staff account for {$user->email} created successfully."); }
    public function status(Request $request, User $user, AuditLogger $audit) { $data=$request->validate(['status'=>['required','in:active,inactive,suspended']]); abort_if($user->is(auth()->user()),422,'You cannot change your own account status.'); if($user->role==='admin' && $data['status']!=='active') abort_if(User::where('role','admin')->where('status','active')->count()<=1,422,'The last active administrator cannot be disabled.'); $old=$user->status; DB::transaction(fn()=>[$user->update($data),$audit->log('user.status_changed',$user,'User account status changed.',['status'=>$old],$data)]); return back()->with('success','Account status updated successfully.'); }
}
