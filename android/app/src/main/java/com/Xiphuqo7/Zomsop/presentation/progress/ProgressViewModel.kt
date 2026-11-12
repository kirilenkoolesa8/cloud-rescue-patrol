package com.Xiphuqo7.Zomsop.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.data.sample.SampleData
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProgressViewModel(
    private val repository: PatrolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = repository.progress()
            _state.value = ProgressUiState(
                totalRescues = progress.career.totalRescues,
                bestStreak = progress.career.bestStreak,
                levelsCleared = progress.levelsCleared,
                accuracy = progress.career.accuracyPercent,
                stars = progress.totalStars,
                totalStars = GameConfig.TOTAL_STARS,
                rows = SampleData.LEVELS.map { level ->
                    ProgressRow(
                        index = level.index,
                        nameRes = level.nameRes,
                        best = progress.bestOf(level.index),
                        stars = progress.starsOf(level.index),
                        wind = (level.windScale * 4f).roundToInt().coerceIn(1, 5),
                        locked = !progress.isUnlocked(level.index)
                    )
                }
            )
        }
    }

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun cancelReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            repository.reset()
            _state.update { it.copy(confirmReset = false) }
            refresh()
        }
    }
}
