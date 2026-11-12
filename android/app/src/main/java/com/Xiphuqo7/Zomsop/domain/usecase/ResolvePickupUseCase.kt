package com.Xiphuqo7.Zomsop.domain.usecase

import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.domain.model.BalloonColor
import com.Xiphuqo7.Zomsop.domain.model.MissionCard

data class PickupOutcome(
    val card: MissionCard,
    val correct: Boolean,
    val cardCompleted: Boolean,
    val scoreDelta: Int,
    val multiplier: Float,
    val streak: Int
)

class ResolvePickupUseCase {

    operator fun invoke(
        card: MissionCard,
        picked: BalloonColor,
        multiplier: Float,
        streak: Int
    ): PickupOutcome {
        val expected = card.next
        if (expected == null || picked != expected) {
            return PickupOutcome(
                card = card.copy(progress = 0),
                correct = false,
                cardCompleted = false,
                scoreDelta = -GameConfig.WRONG_PICK_PENALTY,
                multiplier = 1f,
                streak = 0
            )
        }
        val advanced = card.copy(progress = card.progress + 1)
        val nextStreak = streak + 1
        val nextMultiplier = (multiplier + GameConfig.MULTIPLIER_STEP)
            .coerceAtMost(GameConfig.MULTIPLIER_MAX)
        val delta = if (advanced.completed) {
            (GameConfig.BALLOON_VALUE * multiplier).toInt()
        } else {
            0
        }
        return PickupOutcome(
            card = advanced,
            correct = true,
            cardCompleted = advanced.completed,
            scoreDelta = delta,
            multiplier = nextMultiplier,
            streak = nextStreak
        )
    }
}
