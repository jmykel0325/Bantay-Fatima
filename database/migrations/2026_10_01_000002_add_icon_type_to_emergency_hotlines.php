<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void
    {
        Schema::table('emergency_hotlines', function (Blueprint $table) {
            $table->string('icon_type', 40)->default('other')->after('organization');
        });
    }

    public function down(): void
    {
        Schema::table('emergency_hotlines', function (Blueprint $table) {
            $table->dropColumn('icon_type');
        });
    }
};
