package com.Xiphuqo7.Zomsop.presentation.levelselect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.core.di.ServiceLocator
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.ui.AccentPill
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.LevelTile
import com.Xiphuqo7.Zomsop.core.ui.ScreenHeader

@Composable
fun LevelSelectRoute(
    reducedMotion: Boolean,
    onHome: () -> Unit,
    onLevel: (Int) -> Unit
) {
    val viewModel: LevelSelectViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    LevelSelectScreen(
        state = state,
        reducedMotion = reducedMotion,
        onHome = onHome,
        onLevel = onLevel
    )
}

@Composable
fun LevelSelectScreen(
    state: LevelSelectUiState,
    reducedMotion: Boolean,
    onHome: () -> Unit,
    onLevel: (Int) -> Unit
) {
    val lockedState = stringResource(R.string.state_locked)
    val unlockedState = stringResource(R.string.state_unlocked)

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.78f),
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
                title = stringResource(R.string.title_level_select),
                onHome = onHome,
                trailing = {
                    AccentPill(
                        text = stringResource(
                            R.string.value_stars,
                            state.totalStars,
                            GameConfig.TOTAL_STARS
                        ),
                        accent = AccentSecondary,
                        iconRes = R.drawable.ic_star
                    )
                }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.levels, key = { it.index }) { row ->
                    LevelTile(
                        number = stringResource(R.string.value_plain, row.index + 1),
                        title = stringResource(row.nameRes),
                        stars = row.stars,
                        windLabel = stringResource(R.string.value_wind, row.wind),
                        bestLabel = stringResource(R.string.value_best, row.best),
                        locked = row.locked,
                        lockedHint = stringResource(R.string.locked_hint, row.index),
                        stateText = if (row.locked) lockedState else unlockedState,
                        onClick = { onLevel(row.index) }
                    )
                }
            }
        }
    }
}
