<x-layouts.app title="Administrator Sign In — Bantay Fatima" description="Secure administrator access for Bantay Fatima.">
    <main class="flex min-h-screen items-center justify-center bg-surface px-4 py-10">
        <div class="w-full max-w-md rounded-2xl bg-white p-6 shadow-sm ring-1 ring-navy/10 sm:p-10">
            <div class="flex justify-center"><x-brand-lockup size="lg" stack="always" :show-seal="true" /></div>
            <h1 class="mt-6 text-center text-3xl font-bold text-navy">Administrator Sign In</h1>
            <p class="mt-2 text-center text-sm text-navy/60">Secure access for authorized Barangay Fatima administrators.</p>
            @if($errors->any())<div class="mt-6 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700" role="alert">{{ $errors->first() }}</div>@endif
            <form method="POST" action="{{ route('admin.login.store') }}" class="mt-6 space-y-5">@csrf
                <label class="block text-sm font-semibold text-navy">Email or Phone Number<input name="login" value="{{ old('login') }}" autocomplete="username" required autofocus class="mt-1.5 min-h-11 w-full rounded-xl border border-navy/15 px-4"></label>
                <label class="block text-sm font-semibold text-navy">Password<span class="relative mt-1.5 block"><input id="admin-password" type="password" name="password" autocomplete="current-password" required class="min-h-11 w-full rounded-xl border border-navy/15 px-4 pr-16"><button type="button" id="toggle-admin-password" class="absolute inset-y-0 right-0 px-4 text-xs font-semibold text-teal">Show</button></span></label>
                <div class="flex items-center justify-between gap-4 text-sm"><label class="flex items-center gap-2"><input type="checkbox" name="remember" value="1" class="h-4 w-4 rounded"> Remember Me</label><a href="{{ route('home') }}#support" class="font-semibold text-teal">Forgot Password?</a></div>
                <button class="min-h-11 w-full rounded-xl bg-teal px-5 font-bold text-white">Sign In</button>
            </form>
            <a href="{{ route('home') }}" class="mt-6 block text-center text-sm font-semibold text-navy/65">Back to Public Website</a>
        </div>
    </main>
    <script>document.getElementById('toggle-admin-password').addEventListener('click',e=>{const input=document.getElementById('admin-password'),show=input.type==='password';input.type=show?'text':'password';e.currentTarget.textContent=show?'Hide':'Show'});</script>
</x-layouts.app>
