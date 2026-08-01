<?php

namespace Tests\Feature;

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Laravel\Sanctum\Sanctum;
use Tests\TestCase;

class ResidentPortalTest extends TestCase
{
    use RefreshDatabase;

    public function test_public_landing_page_has_no_visible_authentication_links(): void
    {
        $response = $this->get(route('home'))->assertOk()
            ->assertSee('Learn How It Works')
            ->assertSee('Android App Coming Soon')
            ->assertSee('Resident Mobile Application')
            ->assertSee('Staff Mobile Application')
            ->assertSee('Administrator Web Portal');

        $html = $response->getContent();
        $this->assertStringNotContainsString('/admin/login', $html);
        $this->assertStringNotContainsString('Login on Web', $html);
        $this->assertStringNotContainsString('Create Account', $html);
        $this->assertStringNotContainsString('Register', $html);
    }

    public function test_old_resident_and_staff_web_urls_are_unavailable(): void
    {
        foreach (['/login','/register','/resident/dashboard','/resident/reports','/resident/profile','/staff/dashboard'] as $url) {
            $this->get($url)->assertNotFound();
        }
    }

    public function test_mobile_resident_api_remains_role_protected_for_future_android_use(): void
    {
        $resident = User::factory()->create(['role'=>'resident','status'=>'active']);
        Sanctum::actingAs($resident);
        $this->getJson('/api/resident/dashboard')->assertOk()->assertJsonPath('success', true);

        $staff = User::factory()->create(['role'=>'staff','status'=>'active','email'=>'staff@example.com','phone_number'=>'09170000002']);
        Sanctum::actingAs($staff);
        $this->getJson('/api/resident/dashboard')->assertForbidden();
    }
}
