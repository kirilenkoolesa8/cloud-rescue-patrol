package com.Xiphuqo7.Zomsop.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.Xiphuqo7.Zomsop.core.theme.SurfaceGlass
import com.Xiphuqo7.Zomsop.core.theme.TextMuted
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground

@Composable
fun SplashRoute(
    reducedMotion: Boolean,
    onFinished: () -> Unit
) {
    val viewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()
    SplashScreen(finished = finished, reducedMotion = reducedMotion, onFinished = onFinished)
}

@Composable
fun SplashScreen(
    finished: Boolean,
    reducedMotion: Boolean,
    onFinished: () -> Unit
) {
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    val transition = rememberInfiniteTransition()
    val halo by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600),
            repeatMode = RepeatMode.Reverse
        )
    )
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val bands by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val cross by transition.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val haloScale = if (reducedMotion) 1f else halo
    val ringAngle = if (reducedMotion) 0f else spin
    val bandPhase = if (reducedMotion) 0f else bands
    val crossing = if (reducedMotion) 0.5f else cross

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.94f),
            BgBase.copy(alpha = 0.9f),
            BgDeep.copy(alpha = 0.95f)
        ),
        primaryTint = AccentPrimary,
        secondaryTint = AccentPrimary,
        reducedMotion = reducedMotion
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val rows = listOf(0.26f to 0.10f, 0.46f to 0.07f, 0.66f to 0.05f)
            rows.forEachIndexed { index, pair ->
                val shift = ((bandPhase + index * 0.33f) % 1f) * w
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            AccentPrimary.copy(alpha = pair.second),
                            Color.Transparent
                        )
                    ),
                    topLeft = Offset(-w + shift, h * pair.first),
                    size = Size(w, h * 0.06f)
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.sprite_1),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alpha = 0.35f,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
                .size(96.dp)
                .graphicsLayer {
                    translationX = (crossing - 0.5f) * 460.dp.toPx()
                    rotationZ = 14f
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(216.dp)) {
                    val radius = size.minDimension * 0.38f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AccentPrimary.copy(alpha = 0.22f),
                                Color.Transparent
                            )
                        ),
                        radius = radius * 1.6f * haloScale
                    )
                    rotate(ringAngle) {
                        drawArc(
                            color = AccentPrimary.copy(alpha = 0.55f),
                            startAngle = 0f,
                            sweepAngle = 110f,
                            useCenter = false,
                            style = Stroke(width = 3.dp.toPx()),
                            topLeft = Offset(
                                center.x - radius * 1.25f,
                                center.y - radius * 1.25f
                            ),
                            size = Size(radius * 2.5f, radius * 2.5f)
                        )
                        drawArc(
                            color = AccentSecondary.copy(alpha = 0.4f),
                            startAngle = 180f,
                            sweepAngle = 70f,
                            useCenter = false,
                            style = Stroke(width = 3.dp.toPx()),
                            topLeft = Offset(
                                center.x - radius * 1.25f,
                                center.y - radius * 1.25f
                            ),
                            size = Size(radius * 2.5f, radius * 2.5f)
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_emblem),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(168.dp)
                        .graphicsLayer {
                            scaleX = haloScale
                            scaleY = haloScale
                        }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 12.sp,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                color = AccentPrimary,
                trackColor = SurfaceGlass,
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_status),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
