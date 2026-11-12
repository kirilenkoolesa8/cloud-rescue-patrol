package com.Xiphuqo7.Zomsop.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xiphuqo7.Zomsop.R
import com.Xiphuqo7.Zomsop.core.di.ServiceLocator
import com.Xiphuqo7.Zomsop.core.theme.AccentPrimary
import com.Xiphuqo7.Zomsop.core.theme.BgBase
import com.Xiphuqo7.Zomsop.core.theme.BgDeep
import com.Xiphuqo7.Zomsop.core.theme.SurfaceInactive
import com.Xiphuqo7.Zomsop.core.theme.TextPrimary
import com.Xiphuqo7.Zomsop.core.theme.TextSecondary
import com.Xiphuqo7.Zomsop.core.ui.AuroraBackground
import com.Xiphuqo7.Zomsop.core.ui.GlassCard
import com.Xiphuqo7.Zomsop.core.ui.ScreenHeader
import com.Xiphuqo7.Zomsop.core.ui.SegmentedChoice
import com.Xiphuqo7.Zomsop.domain.model.Sensitivity

@Composable
fun SettingsRoute(
    reducedMotion: Boolean,
    onHome: () -> Unit
) {
    val viewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        reducedMotion = reducedMotion,
        onHome = onHome,
        onSensitivity = viewModel::selectSensitivity,
        onMarkers = viewModel::toggleMarkers,
        onMotion = viewModel::toggleReducedMotion,
        onFlareSide = viewModel::selectFlareSide
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    reducedMotion: Boolean,
    onHome: () -> Unit,
    onSensitivity: (Int) -> Unit,
    onMarkers: (Boolean) -> Unit,
    onMotion: (Boolean) -> Unit,
    onFlareSide: (Int) -> Unit
) {
    val settings = state.settings
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)

    AuroraBackground(
        overlay = listOf(
            BgDeep.copy(alpha = 0.78f),
            BgBase.copy(alpha = 0.72f),
            BgDeep.copy(alpha = 0.9f)
        ),
        reducedMotion = reducedMotion
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.title_settings),
                onHome = onHome
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingBlock(
                    title = stringResource(R.string.setting_sensitivity),
                    hint = stringResource(R.string.setting_sensitivity_hint)
                ) {
                    SegmentedChoice(
                        options = listOf(
                            stringResource(R.string.option_low),
                            stringResource(R.string.option_normal),
                            stringResource(R.string.option_high)
                        ),
                        selectedIndex = Sensitivity.entries.indexOf(settings.sensitivity),
                        onSelect = onSensitivity
                    )
                }
                SettingBlock(
                    title = stringResource(R.string.setting_markers),
                    hint = stringResource(R.string.setting_markers_hint)
                ) {
                    SettingSwitch(
                        checked = settings.colorBlindMarkers,
                        onCheckedChange = onMarkers,
                        onLabel = onLabel,
                        offLabel = offLabel
                    )
                }
                SettingBlock(
                    title = stringResource(R.string.setting_motion),
                    hint = stringResource(R.string.setting_motion_hint)
                ) {
                    SettingSwitch(
                        checked = settings.reducedMotion,
                        onCheckedChange = onMotion,
                        onLabel = onLabel,
                        offLabel = offLabel
                    )
                }
                SettingBlock(
                    title = stringResource(R.string.setting_flare_side),
                    hint = stringResource(R.string.setting_flare_side_hint)
                ) {
                    SegmentedChoice(
                        options = listOf(
                            stringResource(R.string.option_left),
                            stringResource(R.string.option_right)
                        ),
                        selectedIndex = if (settings.flareOnRight) 1 else 0,
                        onSelect = onFlareSide
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingBlock(
    title: String,
    hint: String,
    control: @Composable () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.W700
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = hint,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        control()
    }
}

@Composable
private fun SettingSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLabel: String,
    offLabel: String
) {
    val label = if (checked) onLabel else offLabel
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (checked) AccentPrimary else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.W700
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BgDeep,
                checkedTrackColor = AccentPrimary,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceInactive
            ),
            modifier = Modifier.semantics { stateDescription = label }
        )
    }
}
