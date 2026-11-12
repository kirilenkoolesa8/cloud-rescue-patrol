package com.Xiphuqo7.Zomsop.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.di.ServiceLocator
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.DangerRed
import com.Xiphuqo7.Zomsop.core.theme.SuccessGreen
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.PrimaryButton
import com.Xiphuqo7.Zomsop.core.ui.SecondaryButton
import com.Xiphuqo7.Zomsop.core.ui.StarRow
import com.Xiphuqo7.Zomsop.core.ui.StatCardData
import com.Xiphuqo7.Zomsop.core.ui.StatRow
import com.Xiphuqo7.Zomsop.domain.model.RunOutcome
import com.Xiphuqo7.Zomsop.domain.model.RunResult

@Composable
fun GameOverRoute(
    result: RunResult,
    reducedMotion: Boolean,
    onAgain: () -> Unit,
    onLevels: () -> Unit,
    onHome: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(result) {
        viewModel.present(result)
    }
    GameOverScreen(
        state = state,
        fallback = result,
        reducedMotion = reducedMotion,
        onAgain = onAgain,
        onLevels = onLevels,
        onHome = onHome
    )
}

@Composable
fun GameOverScreen(
    state: GameOverUiState,
    fallback: RunResult,
    reducedMotion: Boolean,
    onAgain: () -> Unit,
    onLevels: () -> Unit,
    onHome: () -> Unit
) {
    val result = state.result ?: fallback
    val accent = when (result.outcome) {
        RunOutcome.CLEARED -> SuccessGreen
        RunOutcome.TIMEOUT -> AccentSecondary
        RunOutcome.GROUNDED -> DangerRed
    }
    val verdict = when (result.outcome) {
        RunOutcome.CLEARED -> R.string.verdict_cleared
        RunOutcome.TIMEOUT -> R.string.verdict_timeout
        RunOutcome.GROUNDED -> R.string.verdict_grounded
    }
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        revealed = true
    }
    val counted by animateFloatAsState(
        targetValue = if (revealed) result.score.toFloat() else 0f,
        animationSpec = tween(durationMillis = if (reducedMotion) 1 else 900)
    )

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.88f),
            BgBase.copy(alpha = 0.82f),
            BgDeep.copy(alpha = 0.94f)
        ),
        primaryTint = accent,
        secondaryTint = accent,
        reducedMotion = reducedMotion
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(state.levelNameRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(verdict),
                style = MaterialTheme.typography.headlineMedium,
                color = accent,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.value_plain, counted.toInt()),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                fontSize = 44.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )
            Text(
                text = stringResource(R.string.hud_score),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            if (result.newBest) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AccentSecondary.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, AccentSecondary)
                ) {
                    Text(
                        text = stringResource(R.string.new_best),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            StarRow(earned = result.stars, starSize = 28.dp)
            Spacer(modifier = Modifier.height(20.dp))

            StatRow(
                cards = listOf(
                    StatCardData(
                        label = stringResource(R.string.label_rescues),
                        value = stringResource(R.string.value_plain, result.rescues),
                        accent = AccentPrimary,
                        visible = result.rescues > 0
                    ),
                    StatCardData(
                        label = stringResource(R.string.label_best_streak),
                        value = stringResource(R.string.value_plain, result.bestStreak),
                        accent = AccentSecondary,
                        visible = result.bestStreak > 0
                    ),
                    StatCardData(
                        label = stringResource(R.string.label_time_left),
                        value = stringResource(R.string.value_seconds, result.secondsLeft),
                        accent = SuccessGreen,
                        visible = result.secondsLeft > 0
                    )
                )
            )

            if (result.unlockedNameRes != 0) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = AccentSecondary.copy(alpha = 0.16f),
                    border = BorderStroke(1.dp, AccentSecondary)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.unlocked_banner),
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentSecondary,
                                fontSize = 10.sp
                            )
                            Text(
                                text = stringResource(result.unlockedNameRes),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onAgain,
                iconRes = R.drawable.ic_refresh,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.route_levels),
                    onClick = onLevels,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onHome,
                    iconRes = R.drawable.icon_home,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
