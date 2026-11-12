package com.Xiphuqo7.Zomsop.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.StrokeAccent
import com.Xiphuqo7.Zomsop.core.theme.SurfaceAlt
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary

@Composable
fun SegmentedChoice(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedState = stringResource(R.string.state_selected)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, option ->
            val active = index == selectedIndex
            OutlinedButton(
                onClick = { onSelect(index) },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, if (active) AccentPrimary else StrokeAccent),
                contentPadding = PaddingValues(horizontal = 6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (active) AccentPrimary else SurfaceAlt.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .semantics { if (active) stateDescription = selectedState }
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (active) BgDeep else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W800,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
