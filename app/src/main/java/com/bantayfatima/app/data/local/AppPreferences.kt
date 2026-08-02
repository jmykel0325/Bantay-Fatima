package com.bantayfatima.app.data.local

import android.content.Context

/**
 * Non-sensitive local settings.
 *
 * Kept separate from [TokenStorage]: nothing here is confidential, so it does not
 * need the keystore, and clearing the session must not reset the user's
 * onboarding progress.
 */
class AppPreferences(context: Context) {

    private val preferences =
        context.applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    /** True once the resident has seen (or skipped) the first-run introduction. */
    fun isOnboardingComplete(): Boolean = preferences.getBoolean(ONBOARDING_COMPLETE, false)

    fun setOnboardingComplete() {
        preferences.edit().putBoolean(ONBOARDING_COMPLETE, true).apply()
    }

    private companion object {
        const val ONBOARDING_COMPLETE = "onboarding_complete"
    }
}
