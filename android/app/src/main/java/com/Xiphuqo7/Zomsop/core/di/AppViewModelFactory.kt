package com.Xiphuqo7.Zomsop.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import com.Xiphuqo7.Zomsop.domain.repository.SettingsRepository
import com.Xiphuqo7.Zomsop.domain.usecase.GenerateWaveUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ResolvePickupUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ResolveUnlocksUseCase
import com.Xiphuqo7.Zomsop.domain.usecase.ScoreRunUseCase
import com.Xiphuqo7.Zomsop.presentation.app.AppViewModel
import com.Xiphuqo7.Zomsop.presentation.collection.CollectionViewModel
import com.Xiphuqo7.Zomsop.presentation.game.GameViewModel
import com.Xiphuqo7.Zomsop.presentation.gameover.GameOverViewModel
import com.Xiphuqo7.Zomsop.presentation.levelselect.LevelSelectViewModel
import com.Xiphuqo7.Zomsop.presentation.menu.MenuViewModel
import com.Xiphuqo7.Zomsop.presentation.progress.ProgressViewModel
import com.Xiphuqo7.Zomsop.presentation.settings.SettingsViewModel
import com.Xiphuqo7.Zomsop.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val patrol: PatrolRepository,
    private val settings: SettingsRepository,
    private val generateWave: GenerateWaveUseCase,
    private val resolvePickup: ResolvePickupUseCase,
    private val scoreRun: ScoreRunUseCase,
    private val resolveUnlocks: ResolveUnlocksUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(patrol)
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) ->
                LevelSelectViewModel(patrol)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(patrol, settings, generateWave, resolvePickup, scoreRun, resolveUnlocks)
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(patrol)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(patrol)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(patrol)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settings)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
