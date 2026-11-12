package com.Xiphuqo7.Zomsop.data.repository

import com.Xiphuqo7.Zomsop.data.local.PatrolPreferences
import com.Xiphuqo7.Zomsop.domain.model.PatrolSettings
import com.Xiphuqo7.Zomsop.domain.model.Sensitivity
import com.Xiphuqo7.Zomsop.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: PatrolPreferences
) : SettingsRepository {

    override fun settings(): PatrolSettings {
        val stored = prefs.text(PatrolPreferences.KEY_SENSITIVITY, Sensitivity.NORMAL.name)
        val sensitivity = Sensitivity.entries.firstOrNull { it.name == stored } ?: Sensitivity.NORMAL
        return PatrolSettings(
            sensitivity = sensitivity,
            colorBlindMarkers = prefs.bool(PatrolPreferences.KEY_MARKERS, false),
            reducedMotion = prefs.bool(PatrolPreferences.KEY_MOTION, false),
            flareOnRight = prefs.bool(PatrolPreferences.KEY_FLARE_SIDE, true)
        )
    }

    override fun save(settings: PatrolSettings) {
        prefs.edit {
            putString(PatrolPreferences.KEY_SENSITIVITY, settings.sensitivity.name)
            putBoolean(PatrolPreferences.KEY_MARKERS, settings.colorBlindMarkers)
            putBoolean(PatrolPreferences.KEY_MOTION, settings.reducedMotion)
            putBoolean(PatrolPreferences.KEY_FLARE_SIDE, settings.flareOnRight)
        }
    }
}
