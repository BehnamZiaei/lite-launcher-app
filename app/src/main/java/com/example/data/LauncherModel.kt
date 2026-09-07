package com.example.data

import android.content.Context
import android.content.SharedPreferences

data class AppItem(
    val label: String,
    val packageName: String,
    val activityName: String,
    val isPinned: Boolean = false,
    val isQuickLaunch: Boolean = false,
    val isHidden: Boolean = false
)

data class ContactItem(
    val id: String,
    val lookupKey: String,
    val displayName: String,
    val phoneNumber: String,
    val timesContacted: Int = 0,
    val photoUri: String? = null
)

data class RamStatus(
    val totalBytes: Long = 0L,
    val availBytes: Long = 0L,
    val isLowMemory: Boolean = false,
    val thresholdBytes: Long = 0L
) {
    val usedBytes: Long get() = (totalBytes - availBytes).coerceAtLeast(0L)
    val usedPercent: Int get() = if (totalBytes > 0) ((usedBytes * 100) / totalBytes).toInt() else 0
    val freePercent: Int get() = 100 - usedPercent

    fun formattedTotal(): String = formatBytes(totalBytes)
    fun formattedAvail(): String = formatBytes(availBytes)
    fun formattedUsed(): String = formatBytes(usedBytes)

    companion object {
        fun formatBytes(bytes: Long): String {
            if (bytes <= 0) return "0 MB"
            val mb = bytes / (1024 * 1024)
            return if (mb >= 1024) {
                val gb = mb.toFloat() / 1024f
                String.format(java.util.Locale.US, "%.1f GB", gb)
            } else {
                "$mb MB"
            }
        }
    }
}

class LauncherPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lite_launcher_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PINNED_PACKAGES = "pinned_packages"
        private const val KEY_QUICK_LAUNCH_PACKAGES = "quick_launch_packages"
        private const val KEY_HIDDEN_PACKAGES = "hidden_packages"
        private const val KEY_TEXT_ONLY_MODE = "text_only_mode"
        private const val KEY_SHOW_RAM_STATUS = "show_ram_status"
        private const val KEY_SHOW_RAM_WIDGET = "show_ram_widget"
        private const val KEY_SHOW_QUICK_LAUNCH_BAR = "show_quick_launch_bar"
        private const val KEY_SHOW_CLOCK = "show_clock"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_SHOW_FREQUENT_CONTACTS = "show_frequent_contacts"
        private const val KEY_NOTHING_STYLE = "nothing_style"
    }

    fun getPinnedPackages(): Set<String> {
        return prefs.getStringSet(KEY_PINNED_PACKAGES, emptySet()) ?: emptySet()
    }

    fun setPinnedPackages(packages: Set<String>) {
        prefs.edit().putStringSet(KEY_PINNED_PACKAGES, packages).apply()
    }

    fun getQuickLaunchPackages(): List<String> {
        val raw = prefs.getString(KEY_QUICK_LAUNCH_PACKAGES, null)
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split(",").filter { it.isNotBlank() }
    }

    fun setQuickLaunchPackages(packages: List<String>) {
        prefs.edit().putString(KEY_QUICK_LAUNCH_PACKAGES, packages.joinToString(",")).apply()
    }

    fun getHiddenPackages(): Set<String> {
        return prefs.getStringSet(KEY_HIDDEN_PACKAGES, emptySet()) ?: emptySet()
    }

    fun setHiddenPackages(packages: Set<String>) {
        prefs.edit().putStringSet(KEY_HIDDEN_PACKAGES, packages).apply()
    }

    fun isTextOnlyMode(): Boolean {
        return prefs.getBoolean(KEY_TEXT_ONLY_MODE, false)
    }

    fun setTextOnlyMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_TEXT_ONLY_MODE, enabled).apply()
    }

    fun isShowRamStatus(): Boolean {
        return prefs.getBoolean(KEY_SHOW_RAM_STATUS, true)
    }

    fun setShowRamStatus(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_RAM_STATUS, enabled).apply()
    }

    fun isShowRamWidget(): Boolean {
        return prefs.getBoolean(KEY_SHOW_RAM_WIDGET, true)
    }

    fun setShowRamWidget(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_RAM_WIDGET, enabled).apply()
    }

    fun isShowQuickLaunchBar(): Boolean {
        return prefs.getBoolean(KEY_SHOW_QUICK_LAUNCH_BAR, true)
    }

    fun setShowQuickLaunchBar(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_QUICK_LAUNCH_BAR, enabled).apply()
    }

    fun isShowClock(): Boolean {
        return prefs.getBoolean(KEY_SHOW_CLOCK, true)
    }

    fun setShowClock(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_CLOCK, enabled).apply()
    }

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_DARK_THEME, true) // Pure Black AMOLED by default for max power saving
    }

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
    }

    fun isShowFrequentContacts(): Boolean {
        return prefs.getBoolean(KEY_SHOW_FREQUENT_CONTACTS, true)
    }

    fun setShowFrequentContacts(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_FREQUENT_CONTACTS, enabled).apply()
    }

    fun isNothingStyle(): Boolean {
        return prefs.getBoolean(KEY_NOTHING_STYLE, true) // Enabled by default for Nothing Phone users
    }

    fun setNothingStyle(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTHING_STYLE, enabled).apply()
    }
}
