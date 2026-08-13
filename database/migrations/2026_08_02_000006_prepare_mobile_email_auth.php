<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('phone_number', 16)->nullable()->change();
        });

        Schema::table('email_verification_codes', function (Blueprint $table) {
            $table->timestamp('last_sent_at')->nullable()->after('attempts');
            $table->timestamp('verified_at')->nullable()->after('last_sent_at');
        });
    }

    public function down(): void
    {
        Schema::table('email_verification_codes', function (Blueprint $table) {
            $table->dropColumn(['last_sent_at', 'verified_at']);
        });
    }
};
