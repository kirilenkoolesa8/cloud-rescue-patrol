package com.Xiphuqo7.Zomsop.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xiphuqo7.Zomsop.core.di.ServiceLocator
import com.Xiphuqo7.Zomsop.core.navigation.Screen
import com.Xiphuqo7.Zomsop.presentation.collection.CollectionRoute
import com.Xiphuqo7.Zomsop.presentation.game.GameRoute
import com.Xiphuqo7.Zomsop.presentation.gameover.GameOverRoute
import com.Xiphuqo7.Zomsop.presentation.levelselect.LevelSelectRoute
import com.Xiphuqo7.Zomsop.presentation.menu.MenuRoute
import com.Xiphuqo7.Zomsop.presentation.progress.ProgressRoute
import com.Xiphuqo7.Zomsop.presentation.settings.SettingsRoute
import com.Xiphuqo7.Zomsop.presentation.settings.SettingsViewModel
import com.Xiphuqo7.Zomsop.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()
    val settings by settingsViewModel.state.collectAsStateWithLifecycle()
    val calm = settings.settings.reducedMotion
    val enter = if (calm) 160 else 360
    val exit = if (calm) 160 else 320

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                val appearing = if (calm) {
                    fadeIn(tween(enter))
                } else {
                    fadeIn(tween(enter)) + slideInVertically(tween(enter)) { it / 12 } +
                        scaleIn(tween(enter), initialScale = 0.97f)
                }
                appearing togetherWith fadeOut(tween(exit))
            }
        ) { target ->
            when (target) {
                Screen.Splash -> SplashRoute(
                    reducedMotion = calm,
                    onFinished = { appViewModel.replace(Screen.Menu) }
                )

                Screen.Menu -> MenuRoute(
                    reducedMotion = calm,
                    onPlay = { level -> appViewModel.navigate(Screen.Game(level)) },
                    onLevels = { appViewModel.navigate(Screen.LevelSelect) },
                    onCollection = { appViewModel.navigate(Screen.Collection) },
                    onProgress = { appViewModel.navigate(Screen.Progress) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                Screen.LevelSelect -> LevelSelectRoute(
                    reducedMotion = calm,
                    onHome = { appViewModel.resetTo(Screen.Menu) },
                    onLevel = { level -> appViewModel.navigate(Screen.Game(level)) }
                )

                is Screen.Game -> GameRoute(
                    levelIndex = target.levelIndex,
                    reducedMotion = calm,
                    onHome = { appViewModel.resetTo(Screen.Menu) },
                    onFinished = { result -> appViewModel.replace(Screen.GameOver(result)) }
                )

                is Screen.GameOver -> GameOverRoute(
                    result = target.result,
                    reducedMotion = calm,
                    onAgain = { appViewModel.replace(Screen.Game(target.result.levelIndex)) },
                    onLevels = { appViewModel.openFromResult(Screen.LevelSelect) },
                    onHome = { appViewModel.openFromResult(Screen.Menu) }
                )

                Screen.Collection -> CollectionRoute(
                    reducedMotion = calm,
                    onHome = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.Progress -> ProgressRoute(
                    reducedMotion = calm,
                    onHome = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.Settings -> SettingsRoute(
                    reducedMotion = calm,
                    onHome = { appViewModel.resetTo(Screen.Menu) }
                )
            }
        }
    }
}
