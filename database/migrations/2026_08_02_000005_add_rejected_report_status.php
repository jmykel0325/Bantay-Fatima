<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    public function up(): void
    {
        if (DB::getDriverName() === 'mysql') {
            DB::statement("ALTER TABLE reports MODIFY status ENUM('pending','in_progress','resolved','rejected') NOT NULL DEFAULT 'pending'");
            DB::table('reports')->where('validation_status', 'rejected')->update(['status' => 'rejected']);
        }
    }

    public function down(): void
    {
        if (DB::getDriverName() === 'mysql') {
            DB::table('reports')->where('status', 'rejected')->update(['status' => 'pending']);
            DB::statement("ALTER TABLE reports MODIFY status ENUM('pending','in_progress','resolved') NOT NULL DEFAULT 'pending'");
        }
    }
};
