package com.Xiphuqo7.Zomsop.domain.repository

import com.Xiphuqo7.Zomsop.domain.model.PatrolSettings

interface SettingsRepository {
    fun settings(): PatrolSettings

    fun save(settings: PatrolSettings)
}
