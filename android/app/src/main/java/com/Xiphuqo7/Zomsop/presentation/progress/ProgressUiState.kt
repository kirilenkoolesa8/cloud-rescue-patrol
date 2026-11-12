package com.Xiphuqo7.Zomsop.presentation.progress

data class ProgressRow(
    val index: Int,
    val nameRes: Int,
    val best: Int,
    val stars: Int,
    val wind: Int,
    val locked: Boolean
)

data class ProgressUiState(
    val totalRescues: Int = 0,
    val bestStreak: Int = 0,
    val levelsCleared: Int = 0,
    val accuracy: Int = 0,
    val stars: Int = 0,
    val totalStars: Int = 18,
    val rows: List<ProgressRow> = emptyList(),
    val confirmReset: Boolean = false
)
