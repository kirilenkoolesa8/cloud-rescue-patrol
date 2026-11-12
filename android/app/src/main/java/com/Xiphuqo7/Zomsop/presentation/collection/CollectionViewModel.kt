package com.Xiphuqo7.Zomsop.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xiphuqo7.Zomsop.data.sample.SampleData
import com.Xiphuqo7.Zomsop.domain.repository.PatrolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val repository: PatrolRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val unlocked = repository.progress().unlocked
            val rows = SampleData.COLLECTION.map { entry ->
                CollectionRow(entry = entry, unlocked = entry.id in unlocked)
            }
            _state.value = CollectionUiState(
                rows = rows,
                unlockedCount = rows.count { it.unlocked },
                total = rows.size
            )
        }
    }

    fun select(id: String) {
        val row = _state.value.rows.firstOrNull { it.entry.id == id } ?: return
        if (row.unlocked) {
            _state.update { current ->
                current.copy(expandedId = if (current.expandedId == id) null else id)
            }
        } else {
            _state.update { current ->
                current.copy(
                    expandedId = null,
                    shakeId = id,
                    shakeTick = current.shakeTick + 1
                )
            }
        }
    }
}
