package com.Xiphuqo7.Zomsop.presentation.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.di.ServiceLocator
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.TextMuted
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AccentPill
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.CollectionCard
import com.Xiphuqo7.Zomsop.core.ui.GlassCard
import com.Xiphuqo7.Zomsop.core.ui.ScreenHeader

@Composable
fun CollectionRoute(
    reducedMotion: Boolean,
    onHome: () -> Unit
) {
    val viewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    CollectionScreen(
        state = state,
        reducedMotion = reducedMotion,
        onHome = onHome,
        onSelect = viewModel::select
    )
}

@Composable
fun CollectionScreen(
    state: CollectionUiState,
    reducedMotion: Boolean,
    onHome: () -> Unit,
    onSelect: (String) -> Unit
) {
    val lockedState = stringResource(R.string.state_locked)
    val unlockedState = stringResource(R.string.state_unlocked)
    val expanded = state.rows.firstOrNull { it.entry.id == state.expandedId }

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.8f),
            BgBase.copy(alpha = 0.74f),
            BgDeep.copy(alpha = 0.92f)
        ),
        reducedMotion = reducedMotion
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.title_collection),
                onHome = onHome,
                trailing = {
                    AccentPill(
                        text = stringResource(
                            R.string.collection_counter,
                            state.unlockedCount,
                            state.total
                        ),
                        accent = AccentPrimary
                    )
                }
            )

            if (state.unlockedCount == 0) {
                Text(
                    text = stringResource(R.string.collection_empty),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )
            }

            AnimatedVisibility(visible = expanded != null) {
                val row = expanded
                if (row != null) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(row.entry.nameRes),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(row.entry.logRes),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.rows, key = { it.entry.id }) { row ->
                    CollectionCard(
                        title = stringResource(row.entry.nameRes),
                        condition = stringResource(row.entry.conditionRes),
                        imageRes = row.entry.imageRes,
                        unlocked = row.unlocked,
                        shakeTrigger = if (state.shakeId == row.entry.id) state.shakeTick else 0,
                        stateText = if (row.unlocked) unlockedState else lockedState,
                        onClick = { onSelect(row.entry.id) }
                    )
                }
            }
        }
    }
}
