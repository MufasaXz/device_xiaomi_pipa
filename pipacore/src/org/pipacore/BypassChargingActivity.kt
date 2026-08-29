/*
 * Copyright (C) 2026 nullpointer1101
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.pipacore.ui.bypass.BypassChargingScreen
import org.pipacore.ui.theme.PipacoreTheme

class BypassChargingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PipacoreTheme {
                BypassChargingScreen(onBackPressed = { finish() })
            }
        }
    }
}
