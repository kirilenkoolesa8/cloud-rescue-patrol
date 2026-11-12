package com.Xiphuqo7.Zomsop.core.di

import android.content.Context
import com.Xiphuqo7.Zomsop.data.local.PatrolPreferences
import com.Xiphuqo7.Zomsop.data.repository.PatrolRepositoryImpl
import com.Xiphuqo7.Zomsop.data.repository.SettingsRepositoryImpl
import com.Xiphuqo7.Zomsop.domain.usecase.GenerateWaveUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ResolvePickupUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ResolveUnlocksUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ScoreRunUseCase

object ServiceLocator {

    private var cached: AppViewModelFactory? = null

    fun init(context: Context) {
        if (cached != null) return
        val prefs = PatrolPreferences(context.applicationContext)
        val unlocks = ResolveUnlocksUseCase()
        cached = AppViewModelFactory(
            patrol = PatrolRepositoryImpl(prefs, unlocks),
            settings = SettingsRepositoryImpl(prefs),
            generateWave = GenerateWaveUseCase(),
            resolvePickup = ResolvePickupUseCase(),
            scoreRun = ScoreRunUseCase(),
            resolveUnlocks = unlocks
        )
    }

    val factory: AppViewModelFactory
        get() = requireNotNull(cached)
}
