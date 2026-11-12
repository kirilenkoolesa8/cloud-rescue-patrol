package com.Xiphuqo7.Zomsop.presentation.gameover

import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome
import com.Xiphuqo7.Zomsop.domain.model.RunResult

data class GameOverUiState(
    val result: RunResult? = null,
    val levelNameRes: Int = R.string.level_1,
    val levelBest: Int = 0,
    val totalStars: Int = 0
) {
    val outcome: RunOutcome
        get() = result?.outcome ?: RunOutcome.TIMEOUT
}
