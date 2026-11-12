package com.Xiphuqo7.Zomsop.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.domain.model.PatrolSettings
import com.Xiphuqo7.Zomsop.domain.model.Sensitivity
import com.Xiphuqo7.Zomsop.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState(repository.settings()))
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    fun selectSensitivity(index: Int) {
        val value = Sensitivity.entries.getOrElse(index) { Sensitivity.NORMAL }
        mutate { it.copy(sensitivity = value) }
    }

    fun toggleMarkers(enabled: Boolean) {
        mutate { it.copy(colorBlindMarkers = enabled) }
    }

    fun toggleReducedMotion(enabled: Boolean) {
        mutate { it.copy(reducedMotion = enabled) }
    }

    fun selectFlareSide(index: Int) {
        mutate { it.copy(flareOnRight = index == 1) }
    }

    private fun mutate(transform: (PatrolSettings) -> PatrolSettings) {
        val updated = transform(_state.value.settings)
        _state.update { it.copy(settings = updated) }
        viewModelScope.launch { repository.save(updated) }
    }
}
