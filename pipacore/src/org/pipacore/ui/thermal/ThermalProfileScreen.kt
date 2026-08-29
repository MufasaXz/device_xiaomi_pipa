/*
 * Copyright (C) 2026 MufasaXz
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore.ui.thermal

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import org.pipacore.R
import org.pipacore.ThermalManager
import org.pipacore.ui.components.*
import org.pipacore.ui.theme.*

@Composable
fun ThermalProfileScreen(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isEnabled by remember { mutableStateOf(ThermalManager.isEnabled(context)) }
    var selectedProfile by remember { mutableStateOf(ThermalManager.getProfile(context)) }

    // Synchronize reactively with QS Tile & external preference changes
    DisposableEffect(context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == ThermalManager.PREF_KEY_ENABLED) {
                isEnabled = ThermalManager.isEnabled(context)
            } else if (key == ThermalManager.PREF_KEY_PROFILE) {
                selectedProfile = ThermalManager.getProfile(context)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    TabletContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            PipacoreTopHeader(
                title = stringResource(R.string.thermal_profile_title),
                subtitle = stringResource(R.string.thermal_profile_summary),
                onBackPressed = onBackPressed
            )

            SectionHeader(title = "Power Management")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SquircleCardShape),
                shape = SquircleCardShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                SettingsSwitchRow(
                    title = stringResource(R.string.thermal_profile_enable_title),
                    summary = if (isEnabled) stringResource(R.string.thermal_profile_enable_summary)
                              else stringResource(R.string.thermal_profile_disabled_summary),
                    checked = isEnabled,
                    enabled = true,
                    onCheckedChange = { newState ->
                        ThermalManager.setEnabled(context, newState)
                        isEnabled = newState
                    }
                )
            }

            SectionHeader(title = "Profile Selection")

            // 1. Performance Profile
            ThermalProfileOptionCard(
                title = stringResource(R.string.thermal_profile_performance),
                summary = stringResource(R.string.thermal_profile_performance_summary),
                iconRes = R.drawable.ic_performance,
                isEnabled = isEnabled,
                isSelected = isEnabled && selectedProfile == ThermalManager.PROFILE_PERFORMANCE,
                onClick = {
                    if (isEnabled) {
                        selectedProfile = ThermalManager.PROFILE_PERFORMANCE
                        ThermalManager.setProfile(context, ThermalManager.PROFILE_PERFORMANCE)
                    }
                }
            )

            // 2. Balanced Profile (Default)
            ThermalProfileOptionCard(
                title = stringResource(R.string.thermal_profile_balanced),
                summary = stringResource(R.string.thermal_profile_balanced_summary),
                iconRes = R.drawable.ic_balanced,
                isEnabled = isEnabled,
                isSelected = isEnabled && selectedProfile == ThermalManager.PROFILE_BALANCED,
                onClick = {
                    if (isEnabled) {
                        selectedProfile = ThermalManager.PROFILE_BALANCED
                        ThermalManager.setProfile(context, ThermalManager.PROFILE_BALANCED)
                    }
                }
            )

            // 3. Battery Saver Profile
            ThermalProfileOptionCard(
                title = stringResource(R.string.thermal_profile_battery),
                summary = stringResource(R.string.thermal_profile_battery_summary),
                iconRes = R.drawable.ic_battery_saver,
                isEnabled = isEnabled,
                isSelected = isEnabled && selectedProfile == ThermalManager.PROFILE_BATTERY,
                onClick = {
                    if (isEnabled) {
                        selectedProfile = ThermalManager.PROFILE_BATTERY
                        ThermalManager.setProfile(context, ThermalManager.PROFILE_BATTERY)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ThermalProfileOptionCard(
    title: String,
    summary: String,
    iconRes: Int,
    isEnabled: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(SquircleCardShape)
            .clickable(enabled = isEnabled) { onClick() }
            .then(
                if (isSelected) Modifier.border(1.5.dp, MaterialTheme.colorScheme.primary, SquircleCardShape)
                else Modifier
            ),
        shape = SquircleCardShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant
                             else if (isEnabled) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                             else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(SquircleIconShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else if (isEnabled) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                           else if (isEnabled) MaterialTheme.colorScheme.onPrimaryContainer
                           else MaterialTheme.colorScheme.outline
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp
                    ),
                    color = if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                enabled = isEnabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.outline,
                    disabledSelectedColor = MaterialTheme.colorScheme.outline,
                    disabledUnselectedColor = Color.Transparent
                )
            )
        }
    }
}
