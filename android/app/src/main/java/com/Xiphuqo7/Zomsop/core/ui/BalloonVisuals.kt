package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.AccentViolet
import com.Xiphuqo7.Zomsop.domain.model.BalloonColor
import com.Xiphuqo7.Zomsop.domain.model.MarkerShape
import com.Xiphuqo7.Zomsop.domain.model.marker

fun balloonTint(color: BalloonColor): Color = when (color) {
    BalloonColor.CYAN -> AccentPrimary
    BalloonColor.AMBER -> AccentSecondary
    BalloonColor.VIOLET -> AccentViolet
}

fun balloonImage(color: BalloonColor): Int = when (color) {
    BalloonColor.CYAN -> R.drawable.sprite_2
    BalloonColor.AMBER -> R.drawable.sprite_3
    BalloonColor.VIOLET -> R.drawable.sprite_4
}

fun balloonDescription(color: BalloonColor): Int = when (color) {
    BalloonColor.CYAN -> R.string.cd_balloon_cyan
    BalloonColor.AMBER -> R.string.cd_balloon_amber
    BalloonColor.VIOLET -> R.string.cd_balloon_violet
}

@Composable
fun MarkerBadge(
    color: BalloonColor,
    modifier: Modifier = Modifier,
    tint: Color = balloonTint(color)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        when (color.marker) {
            MarkerShape.CIRCLE -> drawCircle(
                color = tint,
                radius = w * 0.4f,
                style = Stroke(width = w * 0.16f)
            )

            MarkerShape.TRIANGLE -> {
                val path = Path().apply {
                    moveTo(w * 0.5f, h * 0.12f)
                    lineTo(w * 0.9f, h * 0.86f)
                    lineTo(w * 0.1f, h * 0.86f)
                    close()
                }
                drawPath(path = path, color = tint, style = Stroke(width = w * 0.14f))
            }

            MarkerShape.SQUARE -> drawRect(
                color = tint,
                topLeft = Offset(w * 0.16f, h * 0.16f),
                size = Size(w * 0.68f, h * 0.68f),
                style = Stroke(width = w * 0.15f)
            )
        }
    }
}
