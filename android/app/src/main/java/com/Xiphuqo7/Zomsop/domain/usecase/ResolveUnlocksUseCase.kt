package com.Xiphuqo7.Zomsop.domain.usecase

import com.Xiphuqo7.Zomsop.domain.model.CareerStats
import com.Xiphuqo7.Zomsop.domain.model.PatrolProgress
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome
import com.Xiphuqo7.Zomsop.domain.model.RunTally

object CollectionIds {
    const val CYAN = "balloon_cyan"
    const val AMBER = "balloon_amber"
    const val VIOLET = "balloon_violet"
    const val MK1 = "glider_mk1"
    const val MK2 = "glider_mk2"
    const val MK3 = "glider_mk3"
    const val THERMAL = "thermal_column"
    const val ANVIL = "anvil_cell"
    const val CIRRUS = "night_cirrus"
}

class ResolveUnlocksUseCase {

    operator fun invoke(
        progress: PatrolProgress,
        levelIndex: Int,
        outcome: RunOutcome,
        tally: RunTally
    ): Set<String> {
        val career = merge(progress.career, tally)
        val cleared = outcome == RunOutcome.CLEARED
        val earned = LinkedHashSet<String>()
        if (career.totalRescues >= 1) earned.add(CollectionIds.CYAN)
        if (career.cardsCompleted >= 1) earned.add(CollectionIds.AMBER)
        if (career.longCardsCompleted >= 1) earned.add(CollectionIds.VIOLET)
        if (cleared && levelIndex == 0 || progress.starsOf(0) > 0) earned.add(CollectionIds.MK1)
        if (cleared && levelIndex == 2 || progress.starsOf(2) > 0) earned.add(CollectionIds.MK2)
        if (cleared && levelIndex == 5 || progress.starsOf(5) > 0) earned.add(CollectionIds.MK3)
        if (career.liftFullCount >= 1) earned.add(CollectionIds.THERMAL)
        if (career.flaresFired >= 5) earned.add(CollectionIds.ANVIL)
        if (career.bestMultiplierX100 >= 300) earned.add(CollectionIds.CIRRUS)
        earned.removeAll(progress.unlocked)
        return earned
    }

    fun merge(career: CareerStats, tally: RunTally): CareerStats = career.copy(
        totalRescues = career.totalRescues + tally.rescues,
        cardsCompleted = career.cardsCompleted + tally.cardsCompleted,
        longCardsCompleted = career.longCardsCompleted + tally.longCardsCompleted,
        bestStreak = maxOf(career.bestStreak, tally.bestStreak),
        flaresFired = career.flaresFired + tally.flaresFired,
        liftFullCount = career.liftFullCount + tally.liftFullCount,
        bestMultiplierX100 = maxOf(career.bestMultiplierX100, tally.bestMultiplierX100),
        correctPicks = career.correctPicks + tally.correctPicks,
        totalPicks = career.totalPicks + tally.totalPicks,
        runs = career.runs + 1
    )
}
