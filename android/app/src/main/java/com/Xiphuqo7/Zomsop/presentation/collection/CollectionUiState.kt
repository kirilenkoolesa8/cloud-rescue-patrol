package com.Xiphuqo7.Zomsop.presentation.collection

import com.Xiphuqo7.Zomsop.domain.model.CollectionEntry

data class CollectionRow(
    val entry: CollectionEntry,
    val unlocked: Boolean
)

data class CollectionUiState(
    val rows: List<CollectionRow> = emptyList(),
    val unlockedCount: Int = 0,
    val total: Int = 0,
    val expandedId: String? = null,
    val shakeId: String? = null,
    val shakeTick: Int = 0
)
