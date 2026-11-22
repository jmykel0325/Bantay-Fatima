<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::table('knowledge_chunks', function (Blueprint $table) {
            $table->json('embedding')->nullable()->after('search_text');
            $table->string('embedding_model')->nullable()->after('embedding');
            $table->unsignedSmallInteger('embedding_dimensions')->nullable()->after('embedding_model');
            $table->json('metadata')->nullable()->after('embedding_dimensions');
            $table->index(['embedding_model', 'embedding_dimensions']);
        });
    }
    public function down(): void {
        Schema::table('knowledge_chunks', function (Blueprint $table) {
            $table->dropIndex(['embedding_model', 'embedding_dimensions']);
            $table->dropColumn(['embedding', 'embedding_model', 'embedding_dimensions', 'metadata']);
        });
    }
};
