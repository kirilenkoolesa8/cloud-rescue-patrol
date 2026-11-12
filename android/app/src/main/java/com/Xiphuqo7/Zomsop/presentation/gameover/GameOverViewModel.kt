package com.Xiphuqo7.Zomsop.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.data.sample.SampleData
import com.Xiphuqo7.Zomsop.domain.model.RunResult
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val repository: PatrolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    fun present(result: RunResult) {
        viewModelScope.launch {
            val progress = repository.progress()
            val level = SampleData.LEVELS.getOrElse(result.levelIndex) {
                SampleData.LEVELS.first()
            }
            _state.value = GameOverUiState(
                result = result,
                levelNameRes = level.nameRes,
                levelBest = progress.bestOf(result.levelIndex),
                totalStars = progress.totalStars
            )
        }
    }
}
