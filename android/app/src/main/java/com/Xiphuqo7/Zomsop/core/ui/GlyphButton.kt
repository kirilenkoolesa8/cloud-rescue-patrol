package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.Xiphuqo7.Zomsop.core.theme.StrokeAccent
import com.Xiphuqo7.Zomsop.core.theme.SurfaceGlass

@Composable
fun GlyphButton(
    iconRes: Int,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, StrokeAccent),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceGlass.copy(alpha = 0.82f)
        ),
        modifier = modifier.size(48.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(24.dp)
        )
    }
}
