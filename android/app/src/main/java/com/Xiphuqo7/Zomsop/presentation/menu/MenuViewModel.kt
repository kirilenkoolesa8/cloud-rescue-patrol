package com.Xiphuqo7.Zomsop.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: PatrolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = repository.progress()
            _state.value = MenuUiState(
                bestScore = progress.bestOverall,
                levelsCleared = progress.levelsCleared,
                nextLevelIndex = (progress.unlockedLevels - 1).coerceAtLeast(0),
                hasProgress = progress.bestOverall > 0 || progress.levelsCleared > 0
            )
        }
    }
}
