<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void
    {
        Schema::table('emergency_hotlines', function (Blueprint $table) {
            $table->string('contact_person')->nullable()->after('alternative_number');
            $table->string('availability_type', 20)->default('24_7')->after('contact_person');
            $table->string('coverage_area')->nullable()->after('address');
            $table->string('status', 30)->default('active')->after('is_active');
            $table->boolean('show_in_resident_app')->default(true)->after('status');
            $table->string('priority', 10)->default('normal')->after('show_in_resident_app');
            $table->timestamp('last_verified_at')->nullable()->after('priority');
            $table->foreignId('verified_by')->nullable()->after('last_verified_at')->constrained('users')->nullOnDelete();
        });
    }

    public function down(): void
    {
        Schema::table('emergency_hotlines', function (Blueprint $table) {
            $table->dropForeign(['verified_by']);
            $table->dropColumn(['contact_person','availability_type','coverage_area','status','show_in_resident_app','priority','last_verified_at','verified_by']);
        });
    }
};
