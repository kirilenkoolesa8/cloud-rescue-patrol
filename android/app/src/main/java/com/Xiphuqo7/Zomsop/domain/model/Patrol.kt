package com.Xiphuqo7.Zomsop.domain.model

data class LevelConfig(
    val index: Int,
    val nameRes: Int,
    val windScale: Float,
    val stormLanes: Int,
    val sequenceLength: Int,
    val targetRescues: Int,
    val durationSeconds: Int,
    val parScore: Int,
    val scrollDpPerS: Float
)

enum class RunOutcome { CLEARED, TIMEOUT, GROUNDED }

data class RunResult(
    val levelIndex: Int,
    val outcome: RunOutcome,
    val score: Int,
    val rescues: Int,
    val bestStreak: Int,
    val secondsLeft: Int,
    val stars: Int,
    val newBest: Boolean,
    val unlockedNameRes: Int
)

data class CollectionEntry(
    val id: String,
    val nameRes: Int,
    val logRes: Int,
    val conditionRes: Int,
    val imageRes: Int
)

enum class Sensitivity(val factor: Float) {
    LOW(0.7f),
    NORMAL(1.0f),
    HIGH(1.35f)
}

data class PatrolSettings(
    val sensitivity: Sensitivity = Sensitivity.NORMAL,
    val colorBlindMarkers: Boolean = false,
    val reducedMotion: Boolean = false,
    val flareOnRight: Boolean = true
)

data class CareerStats(
    val totalRescues: Int = 0,
    val cardsCompleted: Int = 0,
    val longCardsCompleted: Int = 0,
    val bestStreak: Int = 0,
    val flaresFired: Int = 0,
    val liftFullCount: Int = 0,
    val bestMultiplierX100: Int = 100,
    val correctPicks: Int = 0,
    val totalPicks: Int = 0,
    val runs: Int = 0
) {
    val accuracyPercent: Int
        get() = if (totalPicks <= 0) 0 else (correctPicks * 100) / totalPicks
}

data class RunTally(
    val rescues: Int,
    val cardsCompleted: Int,
    val longCardsCompleted: Int,
    val bestStreak: Int,
    val flaresFired: Int,
    val liftFullCount: Int,
    val bestMultiplierX100: Int,
    val correctPicks: Int,
    val totalPicks: Int
)

data class PatrolProgress(
    val unlockedLevels: Int = 1,
    val bestScores: List<Int> = emptyList(),
    val stars: List<Int> = emptyList(),
    val career: CareerStats = CareerStats(),
    val unlocked: Set<String> = emptySet()
) {
    val totalStars: Int
        get() = stars.sum()

    val levelsCleared: Int
        get() = stars.count { it > 0 }

    val bestOverall: Int
        get() = bestScores.maxOrNull() ?: 0

    fun bestOf(index: Int): Int = bestScores.getOrElse(index) { 0 }

    fun starsOf(index: Int): Int = stars.getOrElse(index) { 0 }

    fun isUnlocked(index: Int): Boolean = index < unlockedLevels
}
