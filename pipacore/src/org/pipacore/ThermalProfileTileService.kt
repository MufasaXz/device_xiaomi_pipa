/*
 * Copyright (C) 2026 MufasaXz
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class ThermalProfileTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onTileAdded() {
        super.onTileAdded()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        if (!ThermalManager.isEnabled(this)) {
            ThermalManager.setEnabled(this, true)
        } else {
            val currentProfile = ThermalManager.getProfile(this)
            val nextProfile = when (currentProfile) {
                ThermalManager.PROFILE_BALANCED -> ThermalManager.PROFILE_PERFORMANCE
                ThermalManager.PROFILE_PERFORMANCE -> ThermalManager.PROFILE_BATTERY
                else -> ThermalManager.PROFILE_BALANCED
            }
            ThermalManager.setProfile(this, nextProfile)
        }
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val isEnabled = ThermalManager.isEnabled(this)
        val profile = ThermalManager.getProfile(this)
        
        tile.label = getString(R.string.thermal_profile_title)
        if (!isEnabled) {
            tile.state = Tile.STATE_INACTIVE
            tile.subtitle = getString(R.string.status_offline)
            tile.icon = Icon.createWithResource(this, R.drawable.ic_thermal_snowflake)
        } else {
            tile.state = Tile.STATE_ACTIVE
            val iconRes = when (profile) {
                ThermalManager.PROFILE_PERFORMANCE -> {
                    tile.subtitle = getString(R.string.thermal_profile_performance)
                    R.drawable.ic_performance
                }
                ThermalManager.PROFILE_BATTERY -> {
                    tile.subtitle = getString(R.string.thermal_profile_battery)
                    R.drawable.ic_battery_saver
                }
                else -> {
                    tile.subtitle = getString(R.string.thermal_profile_balanced)
                    R.drawable.ic_balanced
                }
            }
            tile.icon = Icon.createWithResource(this, iconRes)
        }
        tile.updateTile()
    }
}
