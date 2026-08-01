<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('first_name')->after('id');
            $table->string('middle_name')->nullable()->after('first_name');
            $table->string('last_name')->after('middle_name');
            $table->string('suffix')->nullable()->after('last_name');
            $table->string('phone_number', 16)->unique()->after('suffix');
            $table->string('firebase_uid')->nullable()->unique()->after('phone_number');
            $table->timestamp('phone_verified_at')->nullable()->after('firebase_uid');
            $table->string('role')->default('resident')->after('password');
            $table->string('status')->default('active')->after('role');
        });

        Schema::table('users', function (Blueprint $table) {
            $table->dropUnique(['email']);
            $table->dropColumn(['name', 'email', 'email_verified_at']);
        });

        Schema::dropIfExists('password_reset_tokens');
    }

    public function down(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('name')->nullable();
            $table->string('email')->nullable()->unique();
            $table->timestamp('email_verified_at')->nullable();
            $table->dropUnique(['phone_number']);
            $table->dropUnique(['firebase_uid']);
            $table->dropColumn([
                'first_name', 'middle_name', 'last_name', 'suffix', 'phone_number',
                'firebase_uid', 'phone_verified_at', 'role', 'status',
            ]);
        });
    }
};
