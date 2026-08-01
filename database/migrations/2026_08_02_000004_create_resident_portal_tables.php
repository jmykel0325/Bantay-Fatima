<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            $table->foreignId('purok_id')->nullable()->after('phone_number')->constrained('puroks')->nullOnDelete();
            $table->string('address')->nullable()->after('purok_id');
        });
        Schema::table('reports', function (Blueprint $table) {
            $table->enum('validation_status', ['unverified','validated','rejected'])->default('unverified')->after('status');
            $table->text('rejection_reason')->nullable()->after('validation_status');
            $table->timestamp('validated_at')->nullable()->after('rejection_reason');
            $table->string('location_notes')->nullable()->after('longitude');
            $table->uuid('submission_token')->nullable()->unique()->after('location_notes');
            $table->text('cancellation_reason')->nullable()->after('submission_token');
            $table->timestamp('cancellation_requested_at')->nullable()->after('cancellation_reason');
            $table->index(['resident_id','status','created_at']);
            $table->index(['validation_status','created_at']);
        });
        Schema::table('report_photos', function (Blueprint $table) {
            $table->string('original_name')->nullable()->after('path');
            $table->string('mime_type', 100)->nullable()->after('original_name');
            $table->unsignedBigInteger('size')->nullable()->after('mime_type');
        });
        Schema::create('advisories', function (Blueprint $table) {
            $table->id(); $table->string('type')->default('advisory'); $table->string('title'); $table->string('summary'); $table->longText('content');
            $table->enum('audience',['residents','staff','everyone'])->default('everyone'); $table->boolean('is_urgent')->default(false);
            $table->enum('status',['draft','scheduled','published','archived'])->default('draft'); $table->string('featured_image_path')->nullable();
            $table->timestamp('published_at')->nullable(); $table->timestamp('expires_at')->nullable(); $table->foreignId('created_by')->nullable()->constrained('users')->nullOnDelete();
            $table->timestamps(); $table->softDeletes(); $table->index(['status','audience','published_at','expires_at']);
        });
        Schema::create('emergency_hotlines', function (Blueprint $table) {
            $table->id(); $table->string('group'); $table->string('organization'); $table->string('hotline'); $table->string('alternative_number')->nullable();
            $table->string('address')->nullable(); $table->string('hours')->nullable(); $table->text('description')->nullable(); $table->boolean('is_active')->default(true);
            $table->unsignedSmallInteger('sort_order')->default(0); $table->timestamps(); $table->softDeletes(); $table->index(['is_active','group','sort_order']);
        });
        Schema::create('notifications', function (Blueprint $table) {
            $table->uuid('id')->primary(); $table->string('type'); $table->morphs('notifiable'); $table->text('data'); $table->timestamp('read_at')->nullable(); $table->timestamps();
        });
        $categories=['Garbage and Waste Disposal','Streetlight','Road Damage','Flooding','Clogged Drainage','Peace and Order','Noise Disturbance','Stray Animals','Public Facility','Other Community Concern'];
        foreach($categories as $name) DB::table('report_categories')->updateOrInsert(['name'=>$name],['description'=>null,'is_active'=>true,'updated_at'=>now(),'created_at'=>now()]);
    }
    public function down(): void
    {
        Schema::dropIfExists('notifications'); Schema::dropIfExists('emergency_hotlines'); Schema::dropIfExists('advisories');
        Schema::table('report_photos', fn(Blueprint $table)=>$table->dropColumn(['original_name','mime_type','size']));
        Schema::table('reports', function(Blueprint $table){$table->dropIndex(['resident_id','status','created_at']);$table->dropIndex(['validation_status','created_at']);$table->dropUnique(['submission_token']);$table->dropColumn(['validation_status','rejection_reason','validated_at','location_notes','submission_token','cancellation_reason','cancellation_requested_at']);});
        Schema::table('users', function(Blueprint $table){$table->dropForeign(['purok_id']);$table->dropColumn(['purok_id','address']);});
    }
};
