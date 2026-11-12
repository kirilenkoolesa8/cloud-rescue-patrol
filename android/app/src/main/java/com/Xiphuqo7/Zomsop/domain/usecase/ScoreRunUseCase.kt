package com.Xiphuqo7.Zomsop.domain.usecase

import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.domain.model.LevelConfig
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome

data class ScoredRun(val score: Int, val stars: Int)

class ScoreRunUseCase {

    operator fun invoke(
        level: LevelConfig,
        outcome: RunOutcome,
        baseScore: Int,
        secondsLeft: Int,
        lift: Float
    ): ScoredRun {
        val bonus = if (outcome == RunOutcome.CLEARED) {
            secondsLeft * GameConfig.TIME_BONUS_PER_SECOND +
                lift.toInt() * GameConfig.LIFT_BONUS_PER_UNIT
        } else {
            0
        }
        val score = (baseScore + bonus).coerceAtLeast(0)
        val stars = if (outcome != RunOutcome.CLEARED) {
            0
        } else {
            var earned = 1
            if (score >= (level.parScore * 0.7f).toInt()) earned++
            if (score >= level.parScore) earned++
            earned
        }
        return ScoredRun(score, stars)
    }
}
