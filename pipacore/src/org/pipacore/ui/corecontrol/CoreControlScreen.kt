/*
 * Copyright (C) 2026 MufasaXz
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore.ui.corecontrol

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.pipacore.CoreControlUtils
import org.pipacore.R
import org.pipacore.ui.components.*
import org.pipacore.ui.theme.*

@Composable
fun CoreControlScreen(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State holders for each core
    var cpu0State by remember { mutableStateOf(true) }
    var cpu1State by remember { mutableStateOf(true) }
    var cpu2State by remember { mutableStateOf(CoreControlUtils.isCoreOnline(2)) }
    var cpu3State by remember { mutableStateOf(CoreControlUtils.isCoreOnline(3)) }
    var cpu4State by remember { mutableStateOf(true) }
    var cpu5State by remember { mutableStateOf(CoreControlUtils.isCoreOnline(5)) }
    var cpu6State by remember { mutableStateOf(CoreControlUtils.isCoreOnline(6)) }
    var cpu7State by remember { mutableStateOf(CoreControlUtils.isCoreOnline(7)) }

    TabletContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            PipacoreTopHeader(
                title = stringResource(R.string.core_control_title),
                subtitle = stringResource(R.string.core_control_summary),
                onBackPressed = onBackPressed
            )

            // CATEGORY 1: Efficiency Cores
            SectionHeader(title = stringResource(R.string.core_control_efficiency_category))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SquircleCardShape),
                shape = SquircleCardShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column {
                    // CPU 0 (Always Active - Hardware design)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu0_title),
                        summary = stringResource(R.string.cpu0_summary),
                        checked = true,
                        enabled = false,
                        lockedStatusText = stringResource(R.string.status_always_active),
                        onCheckedChange = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // CPU 1 (Always Active - Safety lock for stability)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu1_title),
                        summary = stringResource(R.string.cpu1_summary),
                        checked = true,
                        enabled = false,
                        lockedStatusText = stringResource(R.string.status_always_active),
                        onCheckedChange = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // CPU 2 (Toggleable)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu2_title),
                        summary = stringResource(R.string.cpu2_summary),
                        checked = cpu2State,
                        enabled = true,
                        onCheckedChange = { newState ->
                            if (CoreControlUtils.setCoreOnline(2, newState)) {
                                cpu2State = newState
                                CoreControlUtils.saveCoreState(context, 2, newState)
                            }
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // CPU 3 (Toggleable)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu3_title),
                        summary = stringResource(R.string.cpu3_summary),
                        checked = cpu3State,
                        enabled = true,
                        onCheckedChange = { newState ->
                            if (CoreControlUtils.setCoreOnline(3, newState)) {
                                cpu3State = newState
                                CoreControlUtils.saveCoreState(context, 3, newState)
                            }
                        }
                    )
                }
            }

            // CATEGORY 2: Performance Cores
            SectionHeader(title = stringResource(R.string.core_control_performance_category))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SquircleCardShape),
                shape = SquircleCardShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column {
                    // CPU 4 (Always Active - Safety lock for UI responsiveness)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu4_title),
                        summary = stringResource(R.string.cpu4_summary),
                        checked = true,
                        enabled = false,
                        lockedStatusText = stringResource(R.string.status_always_active),
                        onCheckedChange = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // CPU 5 (Toggleable)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu5_title),
                        summary = stringResource(R.string.cpu5_summary),
                        checked = cpu5State,
                        enabled = true,
                        onCheckedChange = { newState ->
                            if (CoreControlUtils.setCoreOnline(5, newState)) {
                                cpu5State = newState
                                CoreControlUtils.saveCoreState(context, 5, newState)
                            }
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // CPU 6 (Toggleable)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu6_title),
                        summary = stringResource(R.string.cpu6_summary),
                        checked = cpu6State,
                        enabled = true,
                        onCheckedChange = { newState ->
                            if (CoreControlUtils.setCoreOnline(6, newState)) {
                                cpu6State = newState
                                CoreControlUtils.saveCoreState(context, 6, newState)
                            }
                        }
                    )
                }
            }

            // CATEGORY 3: Prime Core
            SectionHeader(title = stringResource(R.string.core_control_prime_category))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(SquircleCardShape),
                shape = SquircleCardShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column {
                    // CPU 7 (Prime Turbo Core - Toggleable)
                    SettingsSwitchRow(
                        title = stringResource(R.string.cpu7_title),
                        summary = stringResource(R.string.cpu7_summary),
                        checked = cpu7State,
                        enabled = true,
                        onCheckedChange = { newState ->
                            if (CoreControlUtils.setCoreOnline(7, newState)) {
                                cpu7State = newState
                                CoreControlUtils.saveCoreState(context, 7, newState)
                            }
                        }
                    )
                }
            }

            // FOOTER WARNING BANNER
            WarningBanner(message = stringResource(R.string.core_control_footer_warning))

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
