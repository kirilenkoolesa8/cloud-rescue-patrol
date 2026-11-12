package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary

@Composable
fun StatCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(18.dp),
        borderColor = accent.copy(alpha = 0.35f),
        contentPadding = 12.dp
    ) {
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .background(accent, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = accent,
            fontSize = 22.sp,
            fontWeight = FontWeight.W800,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StatRow(
    cards: List<StatCardData>,
    modifier: Modifier = Modifier
) {
    val visible = cards.filter { it.visible }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visible.forEach { card ->
            StatCard(
                label = card.label,
                value = card.value,
                accent = card.accent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

data class StatCardData(
    val label: String,
    val value: String,
    val accent: Color,
    val visible: Boolean
)
