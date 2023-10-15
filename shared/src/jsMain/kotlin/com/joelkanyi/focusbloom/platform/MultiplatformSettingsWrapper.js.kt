package com.joelkanyi.focusbloom.platform

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual class MultiplatformSettingsWrapper {
    actual fun createSettings(): Settings {
        return StorageSettings()
    }
}