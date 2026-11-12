package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.StrokeAccent
import com.Xiphuqo7.Zomsop.core.theme.SurfaceAlt
import com.Xiphuqo7.Zomsop.core.theme.SurfaceGlass
import com.Xiphuqo7.Zomsop.core.theme.TextMuted
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary

@Composable
fun CollectionCard(
    title: String,
    condition: String,
    imageRes: Int,
    unlocked: Boolean,
    shakeTrigger: Int,
    stateText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shake by animateFloatAsState(
        targetValue = shakeTrigger.toFloat(),
        animationSpec = spring(dampingRatio = 0.22f, stiffness = Spring.StiffnessHigh)
    )
    val offset = (shake - shakeTrigger) * 18f

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, if (unlocked) StrokeAccent else SurfaceAlt),
        contentPadding = PaddingValues(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceGlass.copy(alpha = if (unlocked) 0.86f else 0.5f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.82f)
            .graphicsLayer { translationX = offset }
            .semantics { stateDescription = stateText }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alpha = if (unlocked) 1f else 0.18f,
                    modifier = Modifier.size(54.dp)
                )
                if (!unlocked) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (unlocked) TextPrimary else TextMuted,
                fontSize = 9.sp,
                letterSpacing = 0.6.sp,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = condition,
                style = MaterialTheme.typography.labelSmall,
                color = if (unlocked) AccentPrimary else TextMuted,
                fontSize = 8.sp,
                letterSpacing = 0.4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
            )
        }
    }
}
