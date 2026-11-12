package com.Xiphuqo7.Zomsop.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.StrokeAccent
import com.Xiphuqo7.Zomsop.core.theme.SurfaceGlass
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.PrimaryButton
import com.Xiphuqo7.Zomsop.core.ui.SecondaryButton
import com.Xiphuqo7.Zomsop.core.ui.StatCardData
import com.Xiphuqo7.Zomsop.core.ui.StatRow

@Composable
fun MenuRoute(
    reducedMotion: Boolean,
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    MenuScreen(
        state = state,
        reducedMotion = reducedMotion,
        onPlay = { onPlay(state.nextLevelIndex) },
        onLevels = onLevels,
        onCollection = onCollection,
        onProgress = onProgress,
        onSettings = onSettings
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    reducedMotion: Boolean,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val transition = rememberInfiniteTransition()
    val bob by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val float = if (reducedMotion) 0f else bob

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.2f),
            BgDeep.copy(alpha = 0.45f),
            BgBase.copy(alpha = 0.96f)
        ),
        reducedMotion = reducedMotion
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_emblem),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(104.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_glider),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(132.dp)
                            .graphicsLayer {
                                translationY = float * 10.dp.toPx()
                                rotationZ = float * 4f
                            }
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.58f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = SurfaceGlass.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, StrokeAccent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.menu_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (state.hasProgress) {
                        StatRow(
                            cards = listOf(
                                StatCardData(
                                    label = stringResource(R.string.label_best_score),
                                    value = stringResource(R.string.value_plain, state.bestScore),
                                    accent = AccentSecondary,
                                    visible = true
                                ),
                                StatCardData(
                                    label = stringResource(R.string.label_levels_cleared),
                                    value = stringResource(
                                        R.string.value_plain,
                                        state.levelsCleared
                                    ),
                                    accent = AccentPrimary,
                                    visible = true
                                )
                            )
                        )
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = BgBase.copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, StrokeAccent)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = stringResource(R.string.menu_empty_hint),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryButton(
                        text = stringResource(R.string.play),
                        onClick = onPlay,
                        iconRes = R.drawable.ic_play,
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
                            text = stringResource(R.string.route_collection),
                            onClick = onCollection,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.route_progress),
                            onClick = onProgress,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.route_settings),
                            onClick = onSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
