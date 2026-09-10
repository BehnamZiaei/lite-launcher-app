package com.example.ui

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppItem
import com.example.data.AppSortOrder
import com.example.data.ContactItem
import com.example.data.LauncherRepository
import com.example.data.RamStatus
import com.example.util.CpuStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LauncherUiState(
    val apps: List<AppItem> = emptyList(),
    val frequentContacts: List<ContactItem> = emptyList(),
    val hasContactsPermission: Boolean = false,
    val hasCallPermission: Boolean = false,
    val searchQuery: String = "",
    val ramStatus: RamStatus = RamStatus(),
    val cpuStatus: CpuStatus = CpuStatus(),
    val isTextOnlyMode: Boolean = false,
    val isDarkTheme: Boolean = true,
    val isDynamicColor: Boolean = true,
    val isNothingStyle: Boolean = false,
    val isPixelStyle: Boolean = true,
    val isPagedApps: Boolean = true,
    val showFrequentContacts: Boolean = true,
    val showRamStatus: Boolean = true,
    val showRamWidget: Boolean = true,
    val showCpuWidget: Boolean = true,
    val showQuickLaunchBar: Boolean = true,
    val showClock: Boolean = true,
    val appSortOrder: AppSortOrder = AppSortOrder.ALPHABETICAL_ASC,
    val selectedAppForMenu: AppItem? = null,
    val showSettingsSheet: Boolean = false,
    val showHiddenAppsDialog: Boolean = false,
    val showQuickLaunchPicker: Boolean = false,
    val showAboutDialog: Boolean = false,
    val showReorderSheet: Boolean = false,
    val snackbarMessage: String? = null,
    val isLoading: Boolean = true
) {
    val pinnedApps: List<AppItem>
        get() = apps.filter { it.isPinned && !it.isHidden }

    val quickLaunchApps: List<AppItem>
        get() = apps.filter { it.isQuickLaunch && !it.isHidden }

    val filteredApps: List<AppItem>
        get() = if (searchQuery.isBlank()) {
            apps.filter { !it.isHidden }
        } else {
            apps.filter { !it.isHidden && (it.label.contains(searchQuery, ignoreCase = true) || it.packageName.contains(searchQuery, ignoreCase = true)) }
        }

    val hiddenApps: List<AppItem>
        get() = apps.filter { it.isHidden }

    val appPages: List<List<AppItem>>
        get() {
            val list = filteredApps
            return if (list.isEmpty()) emptyList() else list.chunked(20)
        }
}

class LauncherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LauncherRepository(application)

    private val _uiState = MutableStateFlow(
        LauncherUiState(
            isTextOnlyMode = repository.preferences.isTextOnlyMode(),
            isDarkTheme = repository.preferences.isDarkTheme(),
            isDynamicColor = repository.preferences.isDynamicColor(),
            isNothingStyle = repository.preferences.isNothingStyle(),
            isPixelStyle = repository.preferences.isPixelStyle(),
            isPagedApps = repository.preferences.isPagedApps(),
            showFrequentContacts = repository.preferences.isShowFrequentContacts(),
            showRamStatus = repository.preferences.isShowRamStatus(),
            showRamWidget = repository.preferences.isShowRamWidget(),
            showCpuWidget = repository.preferences.isShowCpuWidget(),
            showQuickLaunchBar = repository.preferences.isShowQuickLaunchBar(),
            showClock = repository.preferences.isShowClock(),
            appSortOrder = repository.preferences.getAppSortOrder()
        )
    )
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        loadApps()
        loadFrequentContacts()
        updateSystemStatus()
    }

    fun loadApps() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val apps = repository.getInstalledApps()
            val ram = repository.getRamStatus()
            val cpu = repository.getCpuStatus()

            // If quick launch is empty on initial setup, seed default essentials (e.g. first 4 non-hidden apps)
            var currentQuickLaunch = repository.preferences.getQuickLaunchPackages()
            if (currentQuickLaunch.isEmpty() && apps.isNotEmpty()) {
                val seed = apps.take(4).map { it.packageName }
                repository.preferences.setQuickLaunchPackages(seed)
                currentQuickLaunch = seed
            }
            val quickSet = currentQuickLaunch.toSet()

            val mappedApps = apps.map { app ->
                app.copy(isQuickLaunch = quickSet.contains(app.packageName))
            }

            _uiState.update {
                it.copy(
                    apps = mappedApps,
                    ramStatus = ram,
                    cpuStatus = cpu,
                    isLoading = false
                )
            }
        }
    }

    fun updateSystemStatus() {
        val ram = repository.getRamStatus()
        val cpu = repository.getCpuStatus()
        _uiState.update { it.copy(ramStatus = ram, cpuStatus = cpu) }
    }

    fun updateRamStatus() {
        updateSystemStatus()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
    }

    fun launchApp(app: AppItem) {
        val success = repository.launchApp(app.packageName, app.activityName)
        if (!success) {
            _uiState.update { it.copy(snackbarMessage = "Could not launch ${app.label}") }
        }
    }

    fun launchTopSearchResult() {
        val firstApp = _uiState.value.filteredApps.firstOrNull()
        if (firstApp != null) {
            launchApp(firstApp)
            clearSearch()
        }
    }

    fun togglePin(app: AppItem) {
        val newPinnedState = !app.isPinned
        val currentPinned = repository.preferences.getPinnedPackages().toMutableSet()
        if (newPinnedState) {
            currentPinned.add(app.packageName)
        } else {
            currentPinned.remove(app.packageName)
        }
        repository.preferences.setPinnedPackages(currentPinned)

        _uiState.update { state ->
            val updatedApps = state.apps.map {
                if (it.packageName == app.packageName) it.copy(isPinned = newPinnedState) else it
            }
            state.copy(
                apps = updatedApps,
                selectedAppForMenu = null
            )
        }
    }

    fun toggleQuickLaunch(app: AppItem) {
        val newQuickLaunchState = !app.isQuickLaunch
        val currentQuickList = repository.preferences.getQuickLaunchPackages().toMutableList()
        if (newQuickLaunchState) {
            if (!currentQuickList.contains(app.packageName)) {
                currentQuickList.add(app.packageName)
            }
        } else {
            currentQuickList.remove(app.packageName)
        }
        repository.preferences.setQuickLaunchPackages(currentQuickList)

        _uiState.update { state ->
            val updatedApps = state.apps.map {
                if (it.packageName == app.packageName) it.copy(isQuickLaunch = newQuickLaunchState) else it
            }
            state.copy(
                apps = updatedApps,
                selectedAppForMenu = null
            )
        }
    }

    fun toggleHide(app: AppItem) {
        val newHiddenState = !app.isHidden
        val currentHidden = repository.preferences.getHiddenPackages().toMutableSet()
        if (newHiddenState) {
            currentHidden.add(app.packageName)
        } else {
            currentHidden.remove(app.packageName)
        }
        repository.preferences.setHiddenPackages(currentHidden)

        _uiState.update { state ->
            val updatedApps = state.apps.map {
                if (it.packageName == app.packageName) it.copy(isHidden = newHiddenState) else it
            }
            state.copy(
                apps = updatedApps,
                selectedAppForMenu = null
            )
        }
    }

    fun setTextOnlyMode(enabled: Boolean) {
        repository.preferences.setTextOnlyMode(enabled)
        if (enabled) {
            repository.clearIconCache()
        }
        _uiState.update { it.copy(isTextOnlyMode = enabled) }
        updateRamStatus()
    }

    fun setShowRamStatus(enabled: Boolean) {
        repository.preferences.setShowRamStatus(enabled)
        _uiState.update { it.copy(showRamStatus = enabled) }
    }

    fun setShowRamWidget(enabled: Boolean) {
        repository.preferences.setShowRamWidget(enabled)
        _uiState.update { it.copy(showRamWidget = enabled) }
    }

    fun setShowCpuWidget(enabled: Boolean) {
        repository.preferences.setShowCpuWidget(enabled)
        _uiState.update { it.copy(showCpuWidget = enabled) }
    }

    fun setAppSortOrder(order: AppSortOrder) {
        repository.preferences.setAppSortOrder(order)
        _uiState.update { it.copy(appSortOrder = order) }
        loadApps()
    }

    fun moveAppUp(app: AppItem) {
        val currentApps = _uiState.value.apps.toMutableList()
        val index = currentApps.indexOfFirst { it.packageName == app.packageName }
        if (index > 0) {
            val prev = currentApps[index - 1]
            currentApps[index - 1] = app
            currentApps[index] = prev
            saveCustomOrder(currentApps)
        }
    }

    fun moveAppDown(app: AppItem) {
        val currentApps = _uiState.value.apps.toMutableList()
        val index = currentApps.indexOfFirst { it.packageName == app.packageName }
        if (index in 0 until currentApps.size - 1) {
            val next = currentApps[index + 1]
            currentApps[index + 1] = app
            currentApps[index] = next
            saveCustomOrder(currentApps)
        }
    }

    fun moveAppToPage(app: AppItem, targetPageIndex: Int) {
        val currentApps = _uiState.value.apps.toMutableList()
        val index = currentApps.indexOfFirst { it.packageName == app.packageName }
        if (index >= 0) {
            currentApps.removeAt(index)
            val targetIndex = (targetPageIndex * 20).coerceIn(0, currentApps.size)
            currentApps.add(targetIndex, app)
            saveCustomOrder(currentApps)
        }
    }

    fun saveCustomOrder(orderedApps: List<AppItem>) {
        val packages = orderedApps.map { it.packageName }
        repository.preferences.setCustomAppOrder(packages)
        repository.preferences.setAppSortOrder(AppSortOrder.CUSTOM)
        _uiState.update {
            it.copy(
                apps = orderedApps,
                appSortOrder = AppSortOrder.CUSTOM
            )
        }
    }

    fun resetAppOrderToDefault() {
        repository.preferences.setCustomAppOrder(emptyList())
        setAppSortOrder(AppSortOrder.ALPHABETICAL_ASC)
    }

    fun toggleReorderSheet(show: Boolean) {
        _uiState.update { it.copy(showReorderSheet = show) }
    }

    fun setShowQuickLaunchBar(enabled: Boolean) {
        repository.preferences.setShowQuickLaunchBar(enabled)
        _uiState.update { it.copy(showQuickLaunchBar = enabled) }
    }

    fun setShowClock(enabled: Boolean) {
        repository.preferences.setShowClock(enabled)
        _uiState.update { it.copy(showClock = enabled) }
    }

    fun selectAppForMenu(app: AppItem?) {
        _uiState.update { it.copy(selectedAppForMenu = app) }
    }

    fun openAppInfo(app: AppItem) {
        repository.openAppInfo(app.packageName)
        _uiState.update { it.copy(selectedAppForMenu = null) }
    }

    fun uninstallApp(app: AppItem) {
        repository.uninstallApp(app.packageName)
        _uiState.update { it.copy(selectedAppForMenu = null) }
    }

    fun openHomeSettings(activity: Activity) {
        repository.openHomeSettings(activity)
    }

    fun trimRam(feedbackMessage: String) {
        repository.clearIconCache()
        updateRamStatus()
        _uiState.update { it.copy(snackbarMessage = feedbackMessage) }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun toggleSettingsSheet(show: Boolean) {
        _uiState.update { it.copy(showSettingsSheet = show) }
    }

    fun toggleHiddenAppsDialog(show: Boolean) {
        _uiState.update { it.copy(showHiddenAppsDialog = show) }
    }

    fun toggleQuickLaunchPicker(show: Boolean) {
        _uiState.update { it.copy(showQuickLaunchPicker = show) }
    }

    fun toggleAboutDialog(show: Boolean) {
        _uiState.update { it.copy(showAboutDialog = show) }
    }

    fun setDarkTheme(enabled: Boolean) {
        repository.preferences.setDarkTheme(enabled)
        _uiState.update { it.copy(isDarkTheme = enabled) }
    }

    fun setDynamicColor(enabled: Boolean) {
        repository.preferences.setDynamicColor(enabled)
        _uiState.update { it.copy(isDynamicColor = enabled) }
    }

    fun setNothingStyle(enabled: Boolean) {
        repository.preferences.setNothingStyle(enabled)
        _uiState.update { it.copy(isNothingStyle = enabled) }
    }

    fun setPixelStyle(enabled: Boolean) {
        repository.preferences.setPixelStyle(enabled)
        _uiState.update { it.copy(isPixelStyle = enabled) }
    }

    fun setPagedApps(enabled: Boolean) {
        repository.preferences.setPagedApps(enabled)
        _uiState.update { it.copy(isPagedApps = enabled) }
    }

    fun setShowFrequentContacts(enabled: Boolean) {
        repository.preferences.setShowFrequentContacts(enabled)
        _uiState.update { it.copy(showFrequentContacts = enabled) }
        if (enabled) {
            loadFrequentContacts()
        }
    }

    fun loadFrequentContacts() {
        viewModelScope.launch {
            val hasPerm = repository.hasContactsPermission()
            val hasCall = repository.hasCallPermission()
            val contacts = if (hasPerm) repository.getFrequentContacts() else emptyList()
            _uiState.update {
                it.copy(
                    hasContactsPermission = hasPerm,
                    hasCallPermission = hasCall,
                    frequentContacts = contacts
                )
            }
        }
    }

    fun updatePermissions() {
        _uiState.update {
            it.copy(
                hasContactsPermission = repository.hasContactsPermission(),
                hasCallPermission = repository.hasCallPermission()
            )
        }
    }

    fun hasCallPermission(): Boolean = repository.hasCallPermission()

    fun callContact(contact: com.example.data.ContactItem) {
        repository.callContact(contact.phoneNumber)
    }

    fun getBitmap(packageName: String) = repository.getAppBitmap(packageName)

    fun getIcon(packageName: String) = repository.getAppIcon(packageName)
}
