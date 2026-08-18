/*
 * Copyright (C) 2024 Paranoid Android
 * Copyright (C) 2026 LineageOS
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.preference.PreferenceManager

class BypassChargingTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        val enabled = BypassChargingUtils.getCurrentHardwareState()
        updateUi(enabled)
    }

    override fun onTileAdded() {
        super.onTileAdded()
        val enabled = BypassChargingUtils.getCurrentHardwareState()
        updateUi(enabled)
    }

    override fun onClick() {
        super.onClick()
        val currentState = BypassChargingUtils.getCurrentHardwareState()
        val newState = !currentState

        BypassChargingUtils.setEnabled(newState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean(BypassChargingUtils.KEY_BYPASS_CHARGING, newState).apply()

        updateUi(newState)
    }

    private fun updateUi(enabled: Boolean) {
        val tile = qsTile ?: return
        tile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = getString(R.string.bypass_charging_title)
        tile.subtitle = null
        tile.icon = Icon.createWithResource(this, R.drawable.ic_bolt)
        tile.updateTile()
    }
}
