package com.Xiphuqo7.Zomsop.data.repository

import com.Xiphuqo7.Zomsop.data.local.PatrolPreferences
import com.Xiphuqo7.Zomsop.data.sample.SampleData
import com.Xiphuqo7.Zomsop.domain.model.CareerStats
import com.Xiphuqo7.Zomsop.domain.model.PatrolProgress
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome
import com.Xiphuqo7.Zomsop.domain.model.RunTally
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import com.Xiphuqo7.Zomsop.domain.usecase.ResolveUnlocksUseCase

class PatrolRepositoryImpl(
    private val prefs: PatrolPreferences,
    private val unlocks: ResolveUnlocksUseCase
) : PatrolRepository {

    private val levelCount = SampleData.LEVELS.size

    override fun progress(): PatrolProgress = PatrolProgress(
        unlockedLevels = prefs.int(PatrolPreferences.KEY_UNLOCKED_LEVELS, 1)
            .coerceIn(1, levelCount),
        bestScores = prefs.list(PatrolPreferences.KEY_BEST_SCORES, levelCount),
        stars = prefs.list(PatrolPreferences.KEY_STARS, levelCount),
        career = CareerStats(
            totalRescues = prefs.int(PatrolPreferences.KEY_TOTAL_RESCUES, 0),
            cardsCompleted = prefs.int(PatrolPreferences.KEY_CARDS, 0),
            longCardsCompleted = prefs.int(PatrolPreferences.KEY_LONG_CARDS, 0),
            bestStreak = prefs.int(PatrolPreferences.KEY_BEST_STREAK, 0),
            flaresFired = prefs.int(PatrolPreferences.KEY_FLARES, 0),
            liftFullCount = prefs.int(PatrolPreferences.KEY_LIFT_FULL, 0),
            bestMultiplierX100 = prefs.int(PatrolPreferences.KEY_BEST_MULT, 100),
            correctPicks = prefs.int(PatrolPreferences.KEY_CORRECT, 0),
            totalPicks = prefs.int(PatrolPreferences.KEY_PICKS, 0),
            runs = prefs.int(PatrolPreferences.KEY_RUNS, 0)
        ),
        unlocked = prefs.ids(PatrolPreferences.KEY_UNLOCKED_CARDS)
    )

    override fun recordRun(
        levelIndex: Int,
        outcome: RunOutcome,
        score: Int,
        stars: Int,
        tally: RunTally,
        unlocked: Set<String>
    ): Boolean {
        val current = progress()
        val previousBest = current.bestOf(levelIndex)
        val newBest = score > previousBest
        val bestScores = current.bestScores.toMutableList()
        val starList = current.stars.toMutableList()
        while (bestScores.size < levelCount) bestScores.add(0)
        while (starList.size < levelCount) starList.add(0)
        if (newBest) bestScores[levelIndex] = score
        if (stars > starList[levelIndex]) starList[levelIndex] = stars
        val unlockedLevels = if (outcome == RunOutcome.CLEARED) {
            maxOf(current.unlockedLevels, (levelIndex + 2).coerceAtMost(levelCount))
        } else {
            current.unlockedLevels
        }
        val career = unlocks.merge(current.career, tally)
        val cards = current.unlocked + unlocked
        prefs.edit {
            putInt(PatrolPreferences.KEY_UNLOCKED_LEVELS, unlockedLevels)
            putString(PatrolPreferences.KEY_BEST_SCORES, PatrolPreferences.join(bestScores))
            putString(PatrolPreferences.KEY_STARS, PatrolPreferences.join(starList))
            putString(PatrolPreferences.KEY_UNLOCKED_CARDS, PatrolPreferences.joinIds(cards))
            putInt(PatrolPreferences.KEY_TOTAL_RESCUES, career.totalRescues)
            putInt(PatrolPreferences.KEY_CARDS, career.cardsCompleted)
            putInt(PatrolPreferences.KEY_LONG_CARDS, career.longCardsCompleted)
            putInt(PatrolPreferences.KEY_BEST_STREAK, career.bestStreak)
            putInt(PatrolPreferences.KEY_FLARES, career.flaresFired)
            putInt(PatrolPreferences.KEY_LIFT_FULL, career.liftFullCount)
            putInt(PatrolPreferences.KEY_BEST_MULT, career.bestMultiplierX100)
            putInt(PatrolPreferences.KEY_CORRECT, career.correctPicks)
            putInt(PatrolPreferences.KEY_PICKS, career.totalPicks)
            putInt(PatrolPreferences.KEY_RUNS, career.runs)
        }
        return newBest
    }

    override fun reset() {
        prefs.edit {
            PROGRESS_KEYS.forEach { remove(it) }
        }
    }

    private companion object {
        val PROGRESS_KEYS = listOf(
            PatrolPreferences.KEY_UNLOCKED_LEVELS,
            PatrolPreferences.KEY_BEST_SCORES,
            PatrolPreferences.KEY_STARS,
            PatrolPreferences.KEY_UNLOCKED_CARDS,
            PatrolPreferences.KEY_TOTAL_RESCUES,
            PatrolPreferences.KEY_CARDS,
            PatrolPreferences.KEY_LONG_CARDS,
            PatrolPreferences.KEY_BEST_STREAK,
            PatrolPreferences.KEY_FLARES,
            PatrolPreferences.KEY_LIFT_FULL,
            PatrolPreferences.KEY_BEST_MULT,
            PatrolPreferences.KEY_CORRECT,
            PatrolPreferences.KEY_PICKS,
            PatrolPreferences.KEY_RUNS
        )
    }
}
