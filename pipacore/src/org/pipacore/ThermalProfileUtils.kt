/*
 * Copyright (C) 2026 MufasaXz
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.pipacore

import android.content.Context

object ThermalProfileUtils {

    const val PREF_KEY_THERMAL_ENABLED = ThermalManager.PREF_KEY_ENABLED
    const val PREF_KEY_THERMAL_PROFILE = ThermalManager.PREF_KEY_PROFILE

    const val PROFILE_BALANCED = ThermalManager.PROFILE_BALANCED
    const val PROFILE_BATTERY = ThermalManager.PROFILE_BATTERY
    const val PROFILE_PERFORMANCE = ThermalManager.PROFILE_PERFORMANCE

    fun isEnabled(context: Context): Boolean {
        return ThermalManager.isEnabled(context)
    }

    fun setEnabled(context: Context, enabled: Boolean) {
        ThermalManager.setEnabled(context, enabled)
    }

    fun getProfile(context: Context): Int {
        return ThermalManager.getProfile(context)
    }

    fun setProfile(context: Context, profile: Int) {
        ThermalManager.setProfile(context, profile)
    }

    fun applyProfile(profile: Int) {
        ThermalManager.applyProfile(profile)
    }

    fun restoreProfile(context: Context) {
        ThermalManager.restoreProfile(context)
    }
}
