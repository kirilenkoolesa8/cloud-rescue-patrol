package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.theme.AccentSecondary
import com.Xiphuqo7.Zomsop.core.theme.StrokeAccent
import com.Xiphuqo7.Zomsop.core.theme.SurfaceAlt
import com.Xiphuqo7.Zomsop.core.theme.SurfaceGlass
import com.Xiphuqo7.Zomsop.core.theme.TextMuted
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary

@Composable
fun LevelTile(
    number: String,
    title: String,
    stars: Int,
    windLabel: String,
    bestLabel: String,
    locked: Boolean,
    lockedHint: String,
    stateText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && !locked) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
    )

    OutlinedButton(
        onClick = onClick,
        enabled = !locked,
        interactionSource = interaction,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (locked) SurfaceAlt else StrokeAccent),
        contentPadding = PaddingValues(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceGlass.copy(alpha = 0.86f),
            disabledContainerColor = SurfaceGlass.copy(alpha = 0.45f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.08f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (locked) 0.55f else 1f
            }
            .semantics { stateDescription = stateText }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = number,
                    color = if (locked) TextMuted else AccentSecondary,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.W800
                )
                if (locked) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    StarRow(earned = stars)
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (locked) TextMuted else TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.fillMaxWidth()
            )
            if (locked) {
                Text(
                    text = lockedHint,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = windLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = bestLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentSecondary,
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
