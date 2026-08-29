/*
 * Copyright (C) 2026 MufasaXz
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.pipacore.ui.theme.PipacoreTheme
import org.pipacore.ui.thermal.ThermalProfileScreen

class ThermalProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PipacoreTheme {
                ThermalProfileScreen(onBackPressed = { finish() })
            }
        }
    }
}
