package com.Xiphuqo7.Zomsop.presentation.game

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.config.GameConfig
import com.Xiphuqo7.Zomsop.core.di.ServiceLocator
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.DangerRed
import com.Xiphuqo7.Zomsop.core.theme.StrokeAccent
import com.Xiphuqo7.Zomsop.core.theme.SurfaceGlass
import com.Xiphuqo7.Zomsop.core.theme.SurfaceInactive
import com.Xiphuqo7.Zomsop.core.theme.TextMuted
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.GlassCard
import com.Xiphuqo7.Zomsop.core.ui.GlyphButton
import com.Xiphuqo7.Zomsop.core.ui.MarkerBadge
import com.Xiphuqo7.Zomsop.core.ui.PrimaryButton
import com.Xiphuqo7.Zomsop.core.ui.SequenceChip
import com.Xiphuqo7.Zomsop.core.ui.balloonDescription
import com.Xiphuqo7.Zomsop.core.ui.balloonImage
import com.Xiphuqo7.Zomsop.domain.model.RunResult
import com.Xiphuqo7.Zomsop.presentation.dialog.PauseDialog
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun GameRoute(
    levelIndex: Int,
    reducedMotion: Boolean,
    onHome: () -> Unit,
    onFinished: (RunResult) -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(levelIndex) {
        viewModel.prepare(levelIndex)
    }
    LaunchedEffect(Unit) {
        viewModel.results.collect { onFinished(it) }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameScreen(
        state = state,
        reducedMotion = reducedMotion,
        onStart = viewModel::start,
        onPause = viewModel::pause,
        onResume = viewModel::resume,
        onRestart = viewModel::restart,
        onHome = onHome,
        onFlare = viewModel::fireFlare,
        onSteer = viewModel::steer,
        onRelease = viewModel::releaseSteer
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    reducedMotion: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onHome: () -> Unit,
    onFlare: () -> Unit,
    onSteer: (Float) -> Unit,
    onRelease: () -> Unit
) {
    val readyLabel = stringResource(R.string.state_ready)
    val chargingLabel = stringResource(R.string.state_charging)

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.55f),
            BgBase.copy(alpha = 0.4f),
            BgDeep.copy(alpha = 0.78f)
        ),
        reducedMotion = reducedMotion
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause, reducedMotion = reducedMotion)
            TimerStrip(state = state)
            Corridor(
                state = state,
                reducedMotion = reducedMotion,
                onSteer = onSteer,
                onRelease = onRelease,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(112.dp))
        }

        if (state.showHint && state.phase == GamePhase.Playing) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 128.dp)
                    .padding(horizontal = 24.dp)
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceGlass.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, StrokeAccent)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.hint_controls),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                }
            }
        }

        if (state.windBanner && state.phase == GamePhase.Playing) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 150.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(12.dp),
                color = AccentPrimary.copy(alpha = 0.22f),
                border = BorderStroke(1.dp, AccentPrimary)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.banner_wind_shift),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentPrimary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
            }
        }

        if (state.scrambled && state.phase == GamePhase.Playing) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 196.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(12.dp),
                color = DangerRed.copy(alpha = 0.22f),
                border = BorderStroke(1.dp, DangerRed)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.banner_scrambled),
                        style = MaterialTheme.typography.labelSmall,
                        color = DangerRed,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                }
            }
        }

        ControlBar(
            state = state,
            readyLabel = readyLabel,
            chargingLabel = chargingLabel,
            onFlare = onFlare,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        )

        if (state.phase == GamePhase.Ready) {
            ReadyOverlay(state = state, onStart = onStart)
        }
    }

    if (state.paused) {
        PauseDialog(
            onResume = onResume,
            onRestart = onRestart,
            onMenu = onHome
        )
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    onPause: () -> Unit,
    reducedMotion: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .background(BgBase.copy(alpha = 0.72f))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GlyphButton(
            iconRes = R.drawable.icon_pause,
            description = stringResource(R.string.cd_pause),
            onClick = onPause
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.card.sequence.forEachIndexed { index, color ->
                SequenceChip(
                    color = color,
                    collected = index < state.card.progress,
                    isNext = index == state.card.progress,
                    showMarker = state.showMarkers,
                    animate = !reducedMotion,
                    chipSize = 32.dp
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = stringResource(R.string.value_plain, state.score),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontSize = 20.sp,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Text(
                text = stringResource(
                    R.string.value_multiplier,
                    formatMultiplier(state.multiplier)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = AccentSecondary,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun TimerStrip(state: GameUiState) {
    val fraction = if (state.totalSeconds <= 0) {
        0f
    } else {
        state.secondsLeft.toFloat() / state.totalSeconds
    }
    val tint = if (state.secondsLeft <= 10) DangerRed else AccentPrimary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgBase.copy(alpha = 0.5f))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(SurfaceInactive, RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .height(5.dp)
                    .background(tint, RoundedCornerShape(3.dp))
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(
                    R.string.hud_rescued,
                    state.rescues,
                    state.targetRescues
                ),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
            Text(
                text = stringResource(R.string.hud_time, state.secondsLeft),
                style = MaterialTheme.typography.labelSmall,
                color = tint,
                fontSize = 10.sp,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun Corridor(
    state: GameUiState,
    reducedMotion: Boolean,
    onSteer: (Float) -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val chevron by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400),
            repeatMode = RepeatMode.Restart
        )
    )
    val chevronPhase = if (reducedMotion) 0f else chevron

    BoxWithConstraints(
        modifier = modifier.pointerInput(state.phase) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull()
                    if (change != null && change.pressed && size.width > 0) {
                        onSteer(change.position.x / size.width.toFloat())
                    } else {
                        onRelease()
                    }
                }
            }
        }
    ) {
        val boxWidth = maxWidth
        val boxHeight = maxHeight

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            for (lane in 1 until GameConfig.LANE_COUNT) {
                val lx = w * lane / GameConfig.LANE_COUNT
                drawRect(
                    color = Color.White.copy(alpha = 0.06f),
                    topLeft = Offset(lx, 0f),
                    size = Size(1f, h)
                )
            }
            val layers = listOf(0.35f to 0.05f, 0.6f to 0.07f, 1f to 0.09f)
            layers.forEachIndexed { index, pair ->
                val speed = pair.first
                val alpha = pair.second
                for (i in 0 until 4) {
                    val base = (state.scroll * speed + i * 0.27f + index * 0.11f) % 1.4f
                    val cy = (base - 0.2f) * h
                    val cx = w * (0.18f + 0.22f * ((i + index) % 4))
                    val radius = w * (0.18f + 0.05f * index)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = alpha),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = radius
                        ),
                        radius = radius,
                        center = Offset(cx, cy)
                    )
                }
            }
            state.windLanes.forEachIndexed { lane, wind ->
                val lx = w * (lane + 0.5f) / GameConfig.LANE_COUNT
                for (i in 0 until 5) {
                    val sy = ((state.scroll * 1.6f + i * 0.2f + lane * 0.07f) % 1f) * h
                    val dx = wind * w * 0.035f
                    drawLine(
                        color = AccentPrimary.copy(alpha = 0.16f),
                        start = Offset(lx - dx, sy),
                        end = Offset(lx + dx, sy + h * 0.02f),
                        strokeWidth = 2f
                    )
                }
            }
            state.thermals.forEach { thermal ->
                val cx = w * thermal.x
                val cy = h * thermal.y
                val halfW = w * GameConfig.THERMAL_HALF_WIDTH
                val halfH = h * GameConfig.THERMAL_HALF_HEIGHT
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AccentPrimary.copy(alpha = 0.26f),
                            Color.Transparent
                        ),
                        startY = cy + halfH,
                        endY = cy - halfH
                    ),
                    topLeft = Offset(cx - halfW, cy - halfH),
                    size = Size(halfW * 2f, halfH * 2f)
                )
                for (i in 0 until 3) {
                    val cheY = cy + halfH - ((chevronPhase + i * 0.33f) % 1f) * halfH * 2f
                    drawLine(
                        color = AccentPrimary.copy(alpha = 0.5f),
                        start = Offset(cx - halfW * 0.5f, cheY + halfW * 0.3f),
                        end = Offset(cx, cheY),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = AccentPrimary.copy(alpha = 0.5f),
                        start = Offset(cx, cheY),
                        end = Offset(cx + halfW * 0.5f, cheY + halfW * 0.3f),
                        strokeWidth = 3f
                    )
                }
            }
            state.storms.forEach { storm ->
                if (!storm.calm) {
                    val cx = w * storm.x
                    val cy = h * storm.y
                    val radius = w * 0.2f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(DangerRed.copy(alpha = 0.22f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = radius
                        ),
                        radius = radius,
                        center = Offset(cx, cy)
                    )
                }
            }
        }

        state.storms.forEach { storm ->
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_storm),
                contentScale = ContentScale.Fit,
                alpha = if (storm.calm) 0.35f else 1f,
                modifier = Modifier
                    .size(96.dp)
                    .offset(
                        x = boxWidth * storm.x - 48.dp,
                        y = boxHeight * storm.y - 48.dp
                    )
                    .graphicsLayer { rotationZ = storm.spin * 57.3f }
            )
        }

        state.balloons.forEach { balloon ->
            val sway = sin((state.scroll * 6f + balloon.sway).toDouble()).toFloat()
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .offset(
                        x = boxWidth * balloon.x - 24.dp + (sway * 6).dp,
                        y = boxHeight * balloon.y - 24.dp
                    )
            ) {
                Image(
                    painter = painterResource(balloonImage(balloon.color)),
                    contentDescription = stringResource(balloonDescription(balloon.color)),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
                if (state.showMarkers) {
                    MarkerBadge(
                        color = balloon.color,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(14.dp)
                    )
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.sprite_1),
            contentDescription = stringResource(R.string.cd_glider),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(72.dp)
                .offset(
                    x = boxWidth * state.gliderX - 36.dp,
                    y = boxHeight * GameConfig.GLIDER_Y - 36.dp
                )
                .graphicsLayer {
                    rotationZ = state.bank + state.crashProgress * 140f
                    alpha = 1f - state.crashProgress * 0.9f
                }
        )

        if (state.crashProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DangerRed.copy(alpha = 0.22f * (1f - state.crashProgress)))
            )
        }
    }
}

@Composable
private fun ControlBar(
    state: GameUiState,
    readyLabel: String,
    chargingLabel: String,
    onFlare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val flareEnabled = state.flareReady && state.phase == GamePhase.Playing
    val gauge: @Composable () -> Unit = {
        Column(modifier = Modifier.width(140.dp)) {
            Text(
                text = stringResource(R.string.hud_lift),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(SurfaceInactive, RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((state.lift / GameConfig.LIFT_MAX).coerceIn(0f, 1f))
                        .height(10.dp)
                        .background(AccentSecondary, RoundedCornerShape(5.dp))
                )
            }
        }
    }
    val flare: @Composable () -> Unit = {
        OutlinedButton(
            onClick = onFlare,
            enabled = flareEnabled,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                width = 2.dp,
                color = if (flareEnabled) AccentSecondary else TextMuted
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (flareEnabled) {
                    AccentSecondary.copy(alpha = 0.22f)
                } else {
                    SurfaceInactive.copy(alpha = 0.38f)
                }
            ),
            modifier = Modifier
                .size(60.dp)
                .semantics {
                    stateDescription = if (flareEnabled) readyLabel else chargingLabel
                }
        ) {
            Text(
                text = stringResource(R.string.flare),
                style = MaterialTheme.typography.labelSmall,
                color = if (flareEnabled) AccentSecondary else TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.W800
            )
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(26.dp),
        color = SurfaceGlass.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, StrokeAccent)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.flareOnRight) {
                gauge()
                Spacer(modifier = Modifier.width(16.dp))
                flare()
            } else {
                flare()
                Spacer(modifier = Modifier.width(16.dp))
                gauge()
            }
        }
    }
}

@Composable
private fun ReadyOverlay(
    state: GameUiState,
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            contentPadding = 20.dp
        ) {
            Text(
                text = stringResource(state.levelNameRes),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.ready_title),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.card.sequence.forEachIndexed { index, color ->
                    SequenceChip(
                        color = color,
                        collected = false,
                        isNext = index == 0,
                        showMarker = state.showMarkers,
                        chipSize = 42.dp,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.hint_controls),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(R.string.go),
                onClick = onStart,
                iconRes = R.drawable.ic_play,
                height = 56.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun formatMultiplier(value: Float): String {
    val scaled = (value * 100f).toInt()
    val whole = scaled / 100
    val fraction = abs(scaled % 100)
    val tail = if (fraction < 10) "0" + fraction else fraction.toString()
    return whole.toString() + "." + tail
}
