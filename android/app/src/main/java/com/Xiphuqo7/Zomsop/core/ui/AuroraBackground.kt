package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.BgDeep

@Composable
fun AuroraBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    primaryTint: Color = AccentPrimary,
    secondaryTint: Color = AccentSecondary,
    reducedMotion: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition()
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val phase = if (reducedMotion) 0.5f else drift

    Box(modifier = modifier.fillMaxSize().background(BgDeep)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val firstCenter = Offset(w * (0.22f + 0.12f * phase), h * (0.24f + 0.05f * phase))
            val firstRadius = w * 0.62f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryTint.copy(alpha = 0.18f), Color.Transparent),
                    center = firstCenter,
                    radius = firstRadius
                ),
                radius = firstRadius,
                center = firstCenter
            )
            val secondCenter = Offset(w * (0.84f - 0.14f * phase), h * (0.62f - 0.08f * phase))
            val secondRadius = w * 0.78f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(secondaryTint.copy(alpha = 0.12f), Color.Transparent),
                    center = secondCenter,
                    radius = secondRadius
                ),
                radius = secondRadius,
                center = secondCenter
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        content()
    }
}
