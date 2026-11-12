package com.Xiphuqo7.Zomsop.presentation.game

import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.domain.model.Balloon
import com.Xiphuqo7.Zomsop.domain.model.MissionCard
import com.Xiphuqo7.Zomsop.domain.model.StormCell
import com.Xiphuqo7.Zomsop.domain.model.Thermal

enum class GamePhase { Ready, Playing, Crashing, Finished }

data class GameUiState(
    val levelIndex: Int = 0,
    val levelNameRes: Int = R.string.level_1,
    val phase: GamePhase = GamePhase.Ready,
    val paused: Boolean = false,
    val card: MissionCard = MissionCard(emptyList()),
    val scrambled: Boolean = false,
    val balloons: List<Balloon> = emptyList(),
    val storms: List<StormCell> = emptyList(),
    val thermals: List<Thermal> = emptyList(),
    val gliderX: Float = 0.5f,
    val bank: Float = 0f,
    val lift: Float = 0f,
    val flareActive: Boolean = false,
    val inThermal: Boolean = false,
    val score: Int = 0,
    val multiplier: Float = 1f,
    val streak: Int = 0,
    val rescues: Int = 0,
    val targetRescues: Int = 0,
    val secondsLeft: Int = 0,
    val totalSeconds: Int = 1,
    val windLanes: List<Float> = emptyList(),
    val windBanner: Boolean = false,
    val showHint: Boolean = true,
    val crashProgress: Float = 0f,
    val scroll: Float = 0f,
    val showMarkers: Boolean = false,
    val flareOnRight: Boolean = true,
    val flareReady: Boolean = false
)
