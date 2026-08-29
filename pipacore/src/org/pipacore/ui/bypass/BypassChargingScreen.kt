/*
 * Copyright (C) 2026 nullpointer1101
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore.ui.bypass

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.pipacore.BypassChargingUtils
import org.pipacore.R
import org.pipacore.ui.components.*
import org.pipacore.ui.theme.*

@Composable
fun BypassChargingScreen(onBackPressed: () -> Unit) {
    val scrollState = rememberScrollState()
    val isSupported = remember { BypassChargingUtils.isSupported() }
    var isEnabled by remember { mutableStateOf(BypassChargingUtils.getCurrentHardwareState()) }

    TabletContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            PipacoreTopHeader(
                title = stringResource(R.string.bypass_charging_title),
                subtitle = stringResource(R.string.bypass_charging_summary),
                onBackPressed = onBackPressed
            )

            SectionHeader(title = "Power Delivery")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SquircleCardShape),
                shape = SquircleCardShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                SettingsSwitchRow(
                    title = stringResource(R.string.bypass_charging_title),
                    summary = if (!isSupported) stringResource(R.string.bypass_charging_not_supported)
                              else if (isEnabled) stringResource(R.string.bypass_charging_status_on)
                              else stringResource(R.string.bypass_charging_status_off),
                    checked = isEnabled,
                    enabled = isSupported,
                    onCheckedChange = { newState ->
                        BypassChargingUtils.setEnabled(newState)
                        isEnabled = newState
                    }
                )
            }

            SectionHeader(title = "Information")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SquircleCardShape),
                shape = SquircleCardShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.bypass_charging_footer),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    ),
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
