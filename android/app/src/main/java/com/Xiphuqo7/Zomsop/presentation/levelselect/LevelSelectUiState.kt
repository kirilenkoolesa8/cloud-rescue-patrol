package com.Xiphuqo7.Zomsop.presentation.levelselect

data class LevelRow(
    val index: Int,
    val nameRes: Int,
    val wind: Int,
    val best: Int,
    val stars: Int,
    val locked: Boolean
)

data class LevelSelectUiState(
    val levels: List<LevelRow> = emptyList(),
    val totalStars: Int = 0
)
