<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class ReportCategory extends Model
{
    protected $fillable = ['name', 'description', 'is_active'];
    protected function casts(): array { return ['is_active' => 'boolean']; }
    public function reports(): HasMany { return $this->hasMany(Report::class, 'category_id'); }
}
