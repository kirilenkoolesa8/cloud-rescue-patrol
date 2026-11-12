package com.Xiphuqo7.Zomsop.presentation.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.AccentViolet
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.SuccessGreen
import com.Xiphuqo7.Zomsop.core.theme.SurfaceInactive
import com.Xiphuqo7.Zomsop.core.theme.TextMuted
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.GlassCard
import com.Xiphuqo7.Zomsop.core.ui.ScreenHeader
import com.Xiphuqo7.Zomsop.core.ui.SecondaryButton
import com.Xiphuqo7.Zomsop.core.ui.StarRow
import com.Xiphuqo7.Zomsop.core.ui.StatCardData
import com.Xiphuqo7.Zomsop.core.ui.StatRow
import com.Xiphuqo7.Zomsop.presentation.dialog.ConfirmDialog

@Composable
fun ProgressRoute(
    reducedMotion: Boolean,
    onHome: () -> Unit
) {
    val viewModel: ProgressViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    ProgressScreen(
        state = state,
        reducedMotion = reducedMotion,
        onHome = onHome,
        onAskReset = viewModel::askReset,
        onCancelReset = viewModel::cancelReset,
        onConfirmReset = viewModel::confirmReset
    )
}

@Composable
fun ProgressScreen(
    state: ProgressUiState,
    reducedMotion: Boolean,
    onHome: () -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
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
                title = stringResource(R.string.title_progress),
                onHome = onHome
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatRow(
                    cards = listOf(
                        StatCardData(
                            label = stringResource(R.string.label_total_rescues),
                            value = stringResource(R.string.value_plain, state.totalRescues),
                            accent = AccentPrimary,
                            visible = true
                        ),
                        StatCardData(
                            label = stringResource(R.string.label_best_streak),
                            value = stringResource(R.string.value_plain, state.bestStreak),
                            accent = AccentSecondary,
                            visible = true
                        )
                    )
                )
                StatRow(
                    cards = listOf(
                        StatCardData(
                            label = stringResource(R.string.label_levels_cleared),
                            value = stringResource(R.string.value_plain, state.levelsCleared),
                            accent = SuccessGreen,
                            visible = true
                        ),
                        StatCardData(
                            label = stringResource(R.string.label_accuracy),
                            value = stringResource(R.string.value_percent, state.accuracy),
                            accent = AccentViolet,
                            visible = true
                        )
                    )
                )
                StarsArc(earned = state.stars, total = state.totalStars)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.rows, key = { it.index }) { row ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        contentPadding = 12.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = stringResource(row.nameRes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (row.locked) TextMuted else TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W700
                                )
                                Text(
                                    text = stringResource(R.string.value_wind, row.wind),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (row.locked) {
                                        stringResource(R.string.value_locked_score)
                                    } else {
                                        stringResource(R.string.value_plain, row.best)
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (row.locked) TextMuted else AccentSecondary,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                StarRow(earned = row.stars, starSize = 14.dp)
                            }
                        }
                    }
                }
            }

            SecondaryButton(
                text = stringResource(R.string.reset_progress),
                onClick = onAskReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }

    if (state.confirmReset) {
        ConfirmDialog(
            title = stringResource(R.string.confirm_reset_title),
            body = stringResource(R.string.confirm_reset_body),
            cancelText = stringResource(R.string.cancel),
            confirmText = stringResource(R.string.reset),
            onCancel = onCancelReset,
            onConfirm = onConfirmReset
        )
    }
}

@Composable
private fun StarsArc(earned: Int, total: Int) {
    val fraction = if (total <= 0) 0f else (earned.toFloat() / total).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val stroke = Stroke(width = 14f, cap = StrokeCap.Round)
            drawArc(
                color = SurfaceInactive,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = stroke
            )
            drawArc(
                color = AccentSecondary,
                startAngle = 135f,
                sweepAngle = 270f * fraction,
                useCenter = false,
                style = stroke
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.value_stars, earned, total),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.label_stars),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}
