package com.Xiphuqo7.Zomsop.presentation.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.data.sample.SampleData
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class LevelSelectViewModel(
    private val repository: PatrolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelSelectUiState())
    val state: StateFlow<LevelSelectUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = repository.progress()
            _state.value = LevelSelectUiState(
                levels = SampleData.LEVELS.map { level ->
                    LevelRow(
                        index = level.index,
                        nameRes = level.nameRes,
                        wind = (level.windScale * 4f).roundToInt().coerceIn(1, 5),
                        best = progress.bestOf(level.index),
                        stars = progress.starsOf(level.index),
                        locked = !progress.isUnlocked(level.index)
                    )
                },
                totalStars = progress.totalStars
            )
        }
    }
}
