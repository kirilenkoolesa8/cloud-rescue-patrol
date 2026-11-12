package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.theme.SuccessGreen
import com.Xiphuqo7.Zomsop.core.theme.SurfaceAlt
import com.Xiphuqo7.Zomsop.domain.model.BalloonColor

@Composable
fun SequenceChip(
    color: BalloonColor,
    collected: Boolean,
    isNext: Boolean,
    showMarker: Boolean,
    modifier: Modifier = Modifier,
    chipSize: Dp = 34.dp,
    animate: Boolean = true
) {
    val transition = rememberInfiniteTransition()
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        )
    )
    val scale = if (isNext && animate && !collected) pulse else 1f
    val tint = balloonTint(color)
    val stateText = if (collected) {
        stringResource(R.string.state_unlocked)
    } else {
        stringResource(R.string.state_ready)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceAlt.copy(alpha = if (collected) 0.45f else 0.92f),
        border = BorderStroke(
            width = if (isNext && !collected) 2.dp else 1.dp,
            color = tint.copy(alpha = if (collected) 0.25f else 0.8f)
        ),
        modifier = modifier
            .size(chipSize)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (collected) 0.45f else 1f
            }
            .semantics { stateDescription = stateText }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(balloonImage(color)),
                contentDescription = stringResource(balloonDescription(color)),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            )
            if (showMarker && !collected) {
                MarkerBadge(
                    color = color,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(13.dp)
                )
            }
            if (collected) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
