package com.Xiphuqo7.Zomsop.core.navigation

import com.Xiphuqo7.Zomsop.domain.model.RunResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object LevelSelect : Screen
    data class Game(val levelIndex: Int) : Screen
    data class GameOver(val result: RunResult) : Screen
    data object Collection : Screen
    data object Progress : Screen
    data object Settings : Screen
}
