package com.Xiphuqo7.Zomsop.presentation.menu

data class MenuUiState(
    val bestScore: Int = 0,
    val levelsCleared: Int = 0,
    val nextLevelIndex: Int = 0,
    val hasProgress: Boolean = false
)
