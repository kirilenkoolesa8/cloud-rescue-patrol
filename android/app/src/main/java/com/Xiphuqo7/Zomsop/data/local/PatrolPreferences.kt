package com.Xiphuqo7.Zomsop.data.local

import android.content.Context
import android.content.SharedPreferences

class PatrolPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun text(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun list(key: String, size: Int): List<Int> {
        val raw = prefs.getString(key, "") ?: ""
        val parsed = raw.split(SEPARATOR).mapNotNull { it.trim().toIntOrNull() }
        return List(size) { parsed.getOrElse(it) { 0 } }
    }

    fun ids(key: String): Set<String> {
        val raw = prefs.getString(key, "") ?: ""
        return raw.split(SEPARATOR).filter { it.isNotBlank() }.toSet()
    }

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        val editor = prefs.edit()
        editor.block()
        editor.apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val STORE = "cloud_rescue_patrol"
        const val SEPARATOR = ","

        const val KEY_UNLOCKED_LEVELS = "unlocked_levels"
        const val KEY_BEST_SCORES = "best_scores"
        const val KEY_STARS = "stars"
        const val KEY_UNLOCKED_CARDS = "unlocked_cards"
        const val KEY_TOTAL_RESCUES = "total_rescues"
        const val KEY_CARDS = "cards_completed"
        const val KEY_LONG_CARDS = "long_cards"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_FLARES = "flares_fired"
        const val KEY_LIFT_FULL = "lift_full"
        const val KEY_BEST_MULT = "best_multiplier"
        const val KEY_CORRECT = "correct_picks"
        const val KEY_PICKS = "total_picks"
        const val KEY_RUNS = "runs"
        const val KEY_SENSITIVITY = "sensitivity"
        const val KEY_MARKERS = "colorblind_markers"
        const val KEY_MOTION = "reduced_motion"
        const val KEY_FLARE_SIDE = "flare_on_right"

        fun join(values: List<Int>): String = values.joinToString(SEPARATOR)

        fun joinIds(values: Set<String>): String = values.joinToString(SEPARATOR)
    }
}
