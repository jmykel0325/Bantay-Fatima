<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\User;
use App\Support\PhilippinePhoneNumber;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\RateLimiter;
use Illuminate\Validation\ValidationException;
use Illuminate\View\View;
use InvalidArgumentException;

class AdminSessionController extends Controller
{
    public function create(Request $request): View|RedirectResponse
    {
        if ($request->user()?->role === 'admin') return redirect()->route('admin.dashboard');

        return view('admin.auth.login');
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $request->validate([
            'login' => ['required', 'string', 'max:255'],
            'password' => ['required', 'string'],
            'remember' => ['sometimes', 'boolean'],
        ]);
        $login = trim($data['login']);
        $key = 'admin-login:'.hash('sha256', mb_strtolower($login).'|'.$request->ip());

        if (RateLimiter::tooManyAttempts($key, 5)) {
            throw ValidationException::withMessages(['login' => 'Too many sign-in attempts. Please try again later.']);
        }

        $query = User::query()->where('role', 'admin')->where('status', 'active');
        if (filter_var($login, FILTER_VALIDATE_EMAIL)) {
            $query->where('email', mb_strtolower($login));
        } else {
            try { $query->where('phone_number', PhilippinePhoneNumber::normalize($login)); }
            catch (InvalidArgumentException) { $query->whereRaw('1 = 0'); }
        }
        $admin = $query->first();

        if (! $admin || ! Hash::check($data['password'], $admin->password)) {
            RateLimiter::hit($key, 60);
            throw ValidationException::withMessages(['login' => 'The provided administrator credentials are incorrect.']);
        }

        if (Auth::check()) Auth::logout();
        RateLimiter::clear($key);
        Auth::login($admin, $request->boolean('remember'));
        $request->session()->regenerate();
        $admin->forceFill(['last_login_at' => now()])->save();

        return redirect()->intended(route('admin.dashboard'));
    }

    public function destroy(Request $request): RedirectResponse
    {
        Auth::logout();
        $request->session()->invalidate();
        $request->session()->regenerateToken();

        return redirect()->route('home');
    }
}
