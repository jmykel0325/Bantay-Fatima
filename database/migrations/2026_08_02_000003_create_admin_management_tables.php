<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->string('position')->nullable()->after('status');
            $table->string('department')->nullable()->after('position');
            $table->boolean('must_change_password')->default(false)->after('department');
            $table->timestamp('last_login_at')->nullable()->after('must_change_password');
            $table->foreignId('created_by')->nullable()->after('last_login_at')->constrained('users')->nullOnDelete();
            $table->softDeletes();
            $table->index(['role', 'status']);
        });

        Schema::create('puroks', function (Blueprint $table) {
            $table->id();
            $table->string('name', 30)->unique();
            $table->boolean('is_active')->default(true);
            $table->unsignedSmallInteger('sort_order')->default(0);
            $table->timestamps();
        });

        Schema::create('report_categories', function (Blueprint $table) {
            $table->id();
            $table->string('name')->unique();
            $table->string('description')->nullable();
            $table->boolean('is_active')->default(true);
            $table->timestamps();
        });

        $puroks = ['1','2','3','4','5','6','7','8','9A','9B','10A','10B','11','11C','11B','11A Upper','11A Lower','12A','13','13A','13B','14','15','16 Upper','16 Lower','17A','17B','18A','18B','18C','20','21','22','23','24','24A','25','28'];
        DB::table('puroks')->insert(collect($puroks)->map(fn (string $name, int $index) => ['name' => $name, 'is_active' => true, 'sort_order' => $index + 1, 'created_at' => now(), 'updated_at' => now()])->all());
        DB::table('report_categories')->insert(collect(['Road and Drainage', 'Waste Management', 'Street Lighting', 'Public Safety', 'Health and Sanitation', 'Other'])->map(fn (string $name) => ['name' => $name, 'is_active' => true, 'created_at' => now(), 'updated_at' => now()])->all());

        Schema::create('reports', function (Blueprint $table) {
            $table->id();
            $table->string('reference_number', 30)->unique();
            $table->foreignId('resident_id')->constrained('users')->restrictOnDelete();
            $table->foreignId('category_id')->constrained('report_categories')->restrictOnDelete();
            $table->foreignId('purok_id')->constrained('puroks')->restrictOnDelete();
            $table->foreignId('assigned_staff_id')->nullable()->constrained('users')->nullOnDelete();
            $table->string('title');
            $table->text('description');
            $table->enum('priority', ['low', 'normal', 'high', 'urgent'])->default('normal');
            $table->enum('status', ['pending', 'in_progress', 'resolved', 'rejected'])->default('pending');
            $table->decimal('latitude', 10, 7)->nullable();
            $table->decimal('longitude', 10, 7)->nullable();
            $table->timestamp('resolved_at')->nullable();
            $table->text('resolution_notes')->nullable();
            $table->string('resolution_photo_path')->nullable();
            $table->timestamps();
            $table->softDeletes();
            $table->index(['status', 'priority', 'created_at']);
        });

        Schema::create('report_photos', function (Blueprint $table) {
            $table->id();
            $table->foreignId('report_id')->constrained()->cascadeOnDelete();
            $table->string('path');
            $table->timestamps();
        });

        Schema::create('report_status_histories', function (Blueprint $table) {
            $table->id();
            $table->foreignId('report_id')->constrained()->cascadeOnDelete();
            $table->foreignId('changed_by')->constrained('users')->restrictOnDelete();
            $table->string('from_status', 30)->nullable();
            $table->string('to_status', 30);
            $table->text('notes')->nullable();
            $table->timestamp('created_at')->useCurrent();
        });

        Schema::create('report_assignments', function (Blueprint $table) {
            $table->id();
            $table->foreignId('report_id')->constrained()->cascadeOnDelete();
            $table->foreignId('staff_id')->constrained('users')->restrictOnDelete();
            $table->foreignId('assigned_by')->constrained('users')->restrictOnDelete();
            $table->timestamp('assigned_at')->useCurrent();
            $table->timestamp('unassigned_at')->nullable();
        });

        Schema::create('report_notes', function (Blueprint $table) {
            $table->id();
            $table->foreignId('report_id')->constrained()->cascadeOnDelete();
            $table->foreignId('user_id')->constrained()->restrictOnDelete();
            $table->text('body');
            $table->boolean('is_resident_visible')->default(false);
            $table->timestamps();
        });

        Schema::create('audit_logs', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->nullable()->constrained()->nullOnDelete();
            $table->string('action');
            $table->string('entity_type')->nullable();
            $table->unsignedBigInteger('entity_id')->nullable();
            $table->text('description');
            $table->json('old_values')->nullable();
            $table->json('new_values')->nullable();
            $table->ipAddress('ip_address')->nullable();
            $table->text('user_agent')->nullable();
            $table->timestamp('created_at')->useCurrent();
            $table->index(['entity_type', 'entity_id']);
            $table->index(['action', 'created_at']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('audit_logs');
        Schema::dropIfExists('report_notes');
        Schema::dropIfExists('report_assignments');
        Schema::dropIfExists('report_status_histories');
        Schema::dropIfExists('report_photos');
        Schema::dropIfExists('reports');
        Schema::dropIfExists('report_categories');
        Schema::dropIfExists('puroks');
        Schema::table('users', function (Blueprint $table) {
            $table->dropForeign(['created_by']);
            $table->dropIndex(['role', 'status']);
            $table->dropColumn(['position', 'department', 'must_change_password', 'last_login_at', 'created_by', 'deleted_at']);
        });
    }
};
