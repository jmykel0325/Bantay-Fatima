<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('email')->unique()->after('suffix');
            $table->timestamp('email_verified_at')->nullable()->after('email');
        });

        Schema::table('users', function (Blueprint $table) {
            $table->dropUnique(['firebase_uid']);
            $table->dropColumn(['firebase_uid', 'phone_verified_at']);
        });

        Schema::create('email_verification_codes', function (Blueprint $table) {
            $table->id();
            $table->string('email');
            $table->string('purpose', 30);
            $table->string('code_hash');
            $table->longText('payload')->nullable();
            $table->unsignedTinyInteger('attempts')->default(0);
            $table->timestamp('expires_at');
            $table->timestamps();
            $table->unique(['email', 'purpose']);
            $table->index('expires_at');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('email_verification_codes');

        Schema::table('users', function (Blueprint $table) {
            $table->string('firebase_uid')->nullable()->unique();
            $table->timestamp('phone_verified_at')->nullable();
            $table->dropUnique(['email']);
            $table->dropColumn(['email', 'email_verified_at']);
        });
    }
};
