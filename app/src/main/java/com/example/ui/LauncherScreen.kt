package com.example.ui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppItem
import com.example.data.RamStatus
import com.example.ui.theme.CleanBg
import com.example.ui.theme.CleanBorder
import com.example.ui.theme.CleanGreen
import com.example.ui.theme.CleanGreenDark
import com.example.ui.theme.CleanSurface
import com.example.ui.theme.CleanSurfaceVariant
import com.example.ui.theme.CleanTextMuted
import com.example.ui.theme.CleanTextPrimary
import com.example.ui.theme.CleanTextSecondary
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PastelAmberBg
import com.example.ui.theme.PastelAmberText
import com.example.ui.theme.PastelBlueBg
import com.example.ui.theme.PastelBlueText
import com.example.ui.theme.PastelCoralBg
import com.example.ui.theme.PastelCoralText
import com.example.ui.theme.PastelGreenBg
import com.example.ui.theme.PastelGreenText
import com.example.ui.theme.PastelPinkBg
import com.example.ui.theme.PastelPinkText
import com.example.ui.theme.PastelPurpleBg
import com.example.ui.theme.PastelPurpleText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import com.example.data.ContactItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadFrequentContacts()
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("launcher_root_scaffold"),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Header Section: Ultra-minimal Large Clock & Date
            if (uiState.showClock && uiState.searchQuery.isBlank()) {
                ClockAndDateHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }

            // Minimalist RAM Widget (Optimized memory badge widget with direct tap-to-trim)
            if (uiState.showRamWidget && uiState.searchQuery.isBlank()) {
                MinimalistRamWidget(
                    ramStatus = uiState.ramStatus,
                    onTrimRam = {
                        val msg = context.getString(R.string.ram_trimmed_message)
                        viewModel.trimRam(msg)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            // Customizable Quick-Launch Bar (Slim and non-intrusive favorite apps dock)
            if (uiState.showQuickLaunchBar && uiState.searchQuery.isBlank()) {
                CustomizableQuickLaunchBar(
                    quickApps = uiState.quickLaunchApps,
                    isTextOnly = uiState.isTextOnlyMode,
                    getIcon = { viewModel.getIcon(it) },
                    onAppClick = { viewModel.launchApp(it) },
                    onAppLongClick = { viewModel.selectAppForMenu(it) },
                    onOpenPicker = { viewModel.toggleQuickLaunchPicker(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }

            // Clean Minimalist Search Bar (pill rounded-28px)
            CleanSearchBarSection(
                query = uiState.searchQuery,
                isDarkTheme = uiState.isDarkTheme,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onClearQuery = { viewModel.clearSearch() },
                onSearchAction = { viewModel.launchTopSearchResult() },
                onToggleDarkTheme = { viewModel.setDarkTheme(!uiState.isDarkTheme) },
                onOpenSettings = { viewModel.toggleSettingsSheet(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            )

            // Main Content: Pinned Apps & Drawer
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("app_list_lazy_column"),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                // Frequent Contacts Row (Auto-displayed on main screen if enabled and search is blank)
                if (uiState.searchQuery.isBlank() && uiState.showFrequentContacts) {
                    item(key = "frequent_contacts_section") {
                        val contacts = uiState.frequentContacts
                        if (uiState.hasContactsPermission) {
                            if (contacts.isNotEmpty()) {
                                CleanSectionHeader(
                                    title = stringResource(R.string.frequent_contacts_title),
                                    count = contacts.size,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                        .testTag("frequent_contacts_row")
                                ) {
                                    items(
                                        items = contacts,
                                        key = { c: ContactItem -> "contact_${c.id}" }
                                    ) { contact ->
                                        CleanContactCard(
                                            contact = contact,
                                            onClick = { viewModel.callContact(contact) }
                                        )
                                    }
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                                    color = CleanBorder
                                )
                            }
                        } else {
                            // Permission banner
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = CleanSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = CleanGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.frequent_contacts_title),
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = CleanTextPrimary
                                        )
                                        Text(
                                            text = stringResource(R.string.contacts_permission_required),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CleanTextSecondary
                                        )
                                    }
                                    TextButton(
                                        onClick = {
                                            contactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
                                        }
                                    ) {
                                        Text(
                                            text = stringResource(R.string.grant_permission),
                                            color = CleanGreen,
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Pinned Apps Row (when search query is blank)
                if (uiState.searchQuery.isBlank() && uiState.pinnedApps.isNotEmpty()) {
                    item(key = "pinned_header") {
                        CleanSectionHeader(
                            title = stringResource(R.string.favorites_title),
                            count = uiState.pinnedApps.size,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                        )
                    }

                    item(key = "pinned_row") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .testTag("pinned_apps_row")
                        ) {
                            items(
                                items = uiState.pinnedApps,
                                key = { "pinned_${it.packageName}" }
                            ) { app ->
                                CleanPinnedAppCard(
                                    app = app,
                                    isTextOnly = uiState.isTextOnlyMode,
                                    getIcon = { viewModel.getIcon(app.packageName) },
                                    onClick = { viewModel.launchApp(app) },
                                    onLongClick = { viewModel.selectAppForMenu(app) }
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                            color = CleanBorder
                        )
                    }
                }

                // Drawer Apps Header
                item(key = "drawer_header") {
                    val title = if (uiState.searchQuery.isBlank()) {
                        stringResource(R.string.all_apps_title)
                    } else {
                        "${stringResource(R.string.all_apps_title)} (${uiState.filteredApps.size})"
                    }

                    CleanSectionHeader(
                        title = title,
                        count = if (uiState.searchQuery.isBlank()) uiState.filteredApps.size else null,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                // Empty State
                if (uiState.filteredApps.isEmpty() && !uiState.isLoading) {
                    item(key = "empty_state") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = CleanTextMuted,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = stringResource(R.string.no_apps_found),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CleanTextSecondary
                                )
                            }
                        }
                    }
                }

                // Drawer App Items
                items(
                    items = uiState.filteredApps,
                    key = { it.packageName }
                ) { app ->
                    CleanAppRowItem(
                        app = app,
                        isTextOnly = uiState.isTextOnlyMode,
                        getIcon = { viewModel.getIcon(app.packageName) },
                        onClick = { viewModel.launchApp(app) },
                        onLongClick = { viewModel.selectAppForMenu(app) },
                        modifier = Modifier.testTag("app_item_${app.packageName}")
                    )
                }
            }

            // Clean Minimalist Status Bar Footer
            CleanFooterStatusBar(
                appCount = uiState.apps.size,
                ramStatus = uiState.ramStatus,
                isTextOnly = uiState.isTextOnlyMode,
                onOpenSettings = { viewModel.toggleSettingsSheet(true) },
                onTrimRam = {
                    val msg = context.getString(R.string.ram_trimmed_message)
                    viewModel.trimRam(msg)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }

    // Context Menu for Selected App (Long Press)
    uiState.selectedAppForMenu?.let { selectedApp ->
        CleanAppContextMenuSheet(
            app = selectedApp,
            onDismiss = { viewModel.selectAppForMenu(null) },
            onTogglePin = { viewModel.togglePin(selectedApp) },
            onToggleQuickLaunch = { viewModel.toggleQuickLaunch(selectedApp) },
            onOpenInfo = { viewModel.openAppInfo(selectedApp) },
            onUninstall = { viewModel.uninstallApp(selectedApp) },
            onToggleHide = { viewModel.toggleHide(selectedApp) }
        )
    }

    // Settings Bottom Sheet
    if (uiState.showSettingsSheet) {
        CleanSettingsBottomSheet(
            uiState = uiState,
            onDismiss = { viewModel.toggleSettingsSheet(false) },
            onToggleTextOnly = { viewModel.setTextOnlyMode(it) },
            onToggleRamWidget = { viewModel.setShowRamWidget(it) },
            onToggleQuickLaunchBar = { viewModel.setShowQuickLaunchBar(it) },
            onToggleShowClock = { viewModel.setShowClock(it) },
            onToggleDarkTheme = { viewModel.setDarkTheme(it) },
            onToggleFrequentContacts = { enabled ->
                viewModel.setShowFrequentContacts(enabled)
                if (enabled && !uiState.hasContactsPermission) {
                    contactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
                }
            },
            onRequestContactsPermission = {
                contactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
            },
            onOpenHomeSettings = {
                (context as? Activity)?.let { act -> viewModel.openHomeSettings(act) }
            },
            onManageQuickLaunch = {
                viewModel.toggleSettingsSheet(false)
                viewModel.toggleQuickLaunchPicker(true)
            },
            onManageHiddenApps = {
                viewModel.toggleSettingsSheet(false)
                viewModel.toggleHiddenAppsDialog(true)
            },
            onTrimRam = {
                val msg = context.getString(R.string.ram_trimmed_message)
                viewModel.trimRam(msg)
            }
        )
    }

    // Quick Launch Customization Picker Sheet
    if (uiState.showQuickLaunchPicker) {
        QuickLaunchCustomizationDialog(
            apps = uiState.apps.filter { !it.isHidden },
            onDismiss = { viewModel.toggleQuickLaunchPicker(false) },
            onToggleQuickLaunch = { viewModel.toggleQuickLaunch(it) }
        )
    }

    // Hidden Apps Management Dialog
    if (uiState.showHiddenAppsDialog) {
        CleanHiddenAppsDialog(
            hiddenApps = uiState.hiddenApps,
            onDismiss = { viewModel.toggleHiddenAppsDialog(false) },
            onUnhide = { viewModel.toggleHide(it) }
        )
    }
}

/**
 * Minimalist RAM Widget
 * Highly optimized, resource-efficient widget displaying current RAM usage percentage,
 * available memory, and direct interactive trim action.
 */
@Composable
fun MinimalistRamWidget(
    ramStatus: RamStatus,
    onTrimRam: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = CleanSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
        modifier = modifier
            .testTag("minimalist_ram_widget")
            .clip(RoundedCornerShape(18.dp))
            .clickable { onTrimRam() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Minimalist RAM percentage ring/badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CleanGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${ramStatus.usedPercent}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = CleanGreenDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.ram_usage_label),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = CleanTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(CleanGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Minimal compact line indicator
                    LinearProgressIndicator(
                        progress = { (ramStatus.usedPercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = CleanGreen,
                        trackColor = CleanSurfaceVariant
                    )
                }
            }

            // Quick RAM status readout & action
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${ramStatus.formattedAvail()} Free",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = CleanTextSecondary
                    )
                    Text(
                        text = stringResource(R.string.clean_ram_action),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = CleanGreen
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.clean_ram_action),
                    tint = CleanTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Customizable Quick-Launch Bar
 * Slim, non-intrusive dock pinned at the top/prominent section for instant access to favorites.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomizableQuickLaunchBar(
    quickApps: List<AppItem>,
    isTextOnly: Boolean,
    getIcon: (String) -> Drawable?,
    onAppClick: (AppItem) -> Unit,
    onAppLongClick: (AppItem) -> Unit,
    onOpenPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = CleanSurface.copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
        modifier = modifier.testTag("quick_launch_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .testTag("quick_launch_items_row"),
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (quickApps.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.quick_launch_add_hint),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = CleanTextMuted,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                } else {
                    items(
                        items = quickApps,
                        key = { "quick_${it.packageName}" }
                    ) { app ->
                        QuickLaunchItem(
                            app = app,
                            isTextOnly = isTextOnly,
                            getIcon = { getIcon(app.packageName) },
                            onClick = { onAppClick(app) },
                            onLongClick = { onAppLongClick(app) }
                        )
                    }
                }
            }

            // Customize / Add Button on the Quick-Launch bar
            IconButton(
                onClick = onOpenPicker,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(CleanSurfaceVariant.copy(alpha = 0.6f))
                    .testTag("quick_launch_customize_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.customize_quick_launch),
                    tint = CleanTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickLaunchItem(
    app: AppItem,
    isTextOnly: Boolean,
    getIcon: () -> Drawable?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag("quick_launch_app_${app.packageName}"),
        contentAlignment = Alignment.Center
    ) {
        if (isTextOnly) {
            CleanSquircleBadge(label = app.label, size = 38)
        } else {
            val icon = remember(app.packageName) { getIcon() }
            if (icon != null) {
                DrawableImage(drawable = icon, modifier = Modifier.size(38.dp))
            } else {
                CleanSquircleBadge(label = app.label, size = 38)
            }
        }
    }
}

@Composable
fun ClockAndDateHeader(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = timeFormatter.format(currentTime),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 64.sp,
                lineHeight = 64.sp,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-2).sp
            ),
            color = CleanTextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dateFormatter.format(currentTime),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            color = CleanTextSecondary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun CleanRamMonitorCard(
    ramStatus: RamStatus,
    onTrimRam: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CleanSurface),
        modifier = modifier
            .border(1.dp, CleanBorder, RoundedCornerShape(20.dp))
            .testTag("ram_monitor_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CleanGreen)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "LITE MODE ACTIVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = CleanTextMuted
                        )
                        Text(
                            text = "RAM Usage: ${ramStatus.usedPercent}% • ${ramStatus.formattedAvail()} Free",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = CleanTextPrimary
                        )
                    }
                }

                IconButton(
                    onClick = onTrimRam,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CleanSurfaceVariant.copy(alpha = 0.5f))
                        .testTag("trim_ram_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.clean_ram_action),
                        tint = CleanTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // RAM Progress Bar
            LinearProgressIndicator(
                progress = { (ramStatus.usedPercent / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CleanGreen,
                trackColor = CleanSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.ram_used_label)}: ${ramStatus.formattedUsed()} / ${ramStatus.formattedTotal()}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = CleanTextMuted
                )
                Text(
                    text = stringResource(R.string.ram_optimization_tip),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                    color = CleanGreenDark
                )
            }
        }
    }
}

@Composable
fun CleanSearchBarSection(
    query: String,
    isDarkTheme: Boolean,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSearchAction: () -> Unit,
    onToggleDarkTheme: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.search_apps_hint),
                    color = CleanTextSecondary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = CleanTextSecondary
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = onClearQuery,
                        modifier = Modifier.testTag("clear_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = CleanTextSecondary
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchAction() }),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CleanSurface,
                unfocusedContainerColor = CleanSurface,
                focusedBorderColor = CleanBorder,
                unfocusedBorderColor = CleanBorder,
                focusedTextColor = CleanTextPrimary,
                unfocusedTextColor = CleanTextPrimary,
                cursorColor = CleanGreen
            ),
            modifier = Modifier
                .weight(1f)
                .testTag("search_apps_input")
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Quick AMOLED Dark Mode Toggle button
        IconButton(
            onClick = onToggleDarkTheme,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CleanSurface)
                .border(1.dp, CleanBorder, CircleShape)
                .testTag("toggle_dark_theme_button")
        ) {
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = stringResource(R.string.theme_pure_black),
                tint = if (isDarkTheme) CleanGreen else CleanTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CleanSurface)
                .border(1.dp, CleanBorder, CircleShape)
                .testTag("launcher_settings_button")
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_title),
                tint = CleanTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun CleanSectionHeader(
    title: String,
    count: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            color = CleanTextSecondary
        )
        if (count != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(CleanSurfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = CleanTextPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CleanPinnedAppCard(
    app: AppItem,
    isTextOnly: Boolean,
    getIcon: () -> Drawable?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = CleanSurface,
        modifier = modifier
            .width(86.dp)
            .height(96.dp)
            .border(1.dp, CleanBorder, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag("pinned_app_${app.packageName}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isTextOnly) {
                CleanSquircleBadge(label = app.label, size = 42)
            } else {
                val icon = remember(app.packageName) { getIcon() }
                if (icon != null) {
                    DrawableImage(drawable = icon, modifier = Modifier.size(42.dp))
                } else {
                    CleanSquircleBadge(label = app.label, size = 42)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = app.label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
                color = CleanTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CleanAppRowItem(
    app: AppItem,
    isTextOnly: Boolean,
    getIcon: () -> Drawable?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isTextOnly) {
            CleanSquircleBadge(label = app.label, size = 48)
        } else {
            val icon = remember(app.packageName) { getIcon() }
            if (icon != null) {
                DrawableImage(drawable = icon, modifier = Modifier.size(48.dp))
            } else {
                CleanSquircleBadge(label = app.label, size = 48)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp
                ),
                color = CleanTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = app.packageName,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = CleanTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (app.isQuickLaunch) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = CleanGreen,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 4.dp)
                )
            }
            if (app.isPinned) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = null,
                    tint = CleanGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CleanSquircleBadge(
    label: String,
    size: Int = 48
) {
    val initial = label.firstOrNull()?.uppercaseChar()?.toString() ?: "#"
    val colorIndex = (label.hashCode() and 0x7FFFFFFF) % cleanPastelPalettes.size
    val (bgColor, textColor) = cleanPastelPalettes[colorIndex]

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size / 3).dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = (size * 0.42f).sp
            ),
            color = textColor
        )
    }
}

private val cleanPastelPalettes = listOf(
    Pair(PastelBlueBg, PastelBlueText),     // Phone / Soft Blue
    Pair(PastelPurpleBg, PastelPurpleText), // Messages / Soft Purple
    Pair(PastelGreenBg, PastelGreenText),   // Camera / Soft Green
    Pair(PastelPinkBg, PastelPinkText),     // Gmail / Soft Pink
    Pair(PastelAmberBg, PastelAmberText),   // Yellow / Amber
    Pair(PastelCoralBg, PastelCoralText)    // Coral / Warm
)

@Composable
fun DrawableImage(
    drawable: Drawable,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(drawable) {
        val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 96
        val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 96
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bmp.asImageBitmap()
    }

    androidx.compose.foundation.Image(
        bitmap = bitmap,
        contentDescription = null,
        modifier = modifier.clip(RoundedCornerShape(14.dp))
    )
}

@Composable
fun CleanFooterStatusBar(
    appCount: Int,
    ramStatus: RamStatus,
    isTextOnly: Boolean,
    onOpenSettings: () -> Unit,
    onTrimRam: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CleanSurfaceVariant.copy(alpha = 0.35f),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "LITE MODE ACTIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = CleanTextMuted
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(CleanGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RAM: ${ramStatus.usedPercent}% (${ramStatus.formattedAvail()} Free)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = CleanTextPrimary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onTrimRam) {
                    Text(
                        text = stringResource(R.string.clean_ram_action),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = CleanGreen
                    )
                }

                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = CleanTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanAppContextMenuSheet(
    app: AppItem,
    onDismiss: () -> Unit,
    onTogglePin: () -> Unit,
    onToggleQuickLaunch: () -> Unit,
    onOpenInfo: () -> Unit,
    onUninstall: () -> Unit,
    onToggleHide: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = CleanSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                CleanSquircleBadge(label = app.label, size = 48)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = app.label,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = CleanTextPrimary
                    )
                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = CleanTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            HorizontalDivider(color = CleanBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Pin to Quick-Launch Bar
            CleanContextMenuItem(
                icon = if (app.isQuickLaunch) Icons.Default.Bolt else Icons.Default.Add,
                title = if (app.isQuickLaunch) stringResource(R.string.action_unpin_quick) else stringResource(R.string.action_pin_quick),
                color = CleanGreen,
                onClick = {
                    onToggleQuickLaunch()
                    onDismiss()
                }
            )

            // Pin / Unpin Home
            CleanContextMenuItem(
                icon = if (app.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                title = if (app.isPinned) stringResource(R.string.action_unpin) else stringResource(R.string.action_pin),
                color = CleanTextPrimary,
                onClick = {
                    onTogglePin()
                    onDismiss()
                }
            )

            // App Info
            CleanContextMenuItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.action_app_info),
                color = CleanTextPrimary,
                onClick = {
                    onOpenInfo()
                    onDismiss()
                }
            )

            // Hide App
            CleanContextMenuItem(
                icon = if (app.isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                title = if (app.isHidden) stringResource(R.string.action_unhide) else stringResource(R.string.action_hide),
                color = CleanTextSecondary,
                onClick = {
                    onToggleHide()
                    onDismiss()
                }
            )

            // Uninstall
            CleanContextMenuItem(
                icon = Icons.Default.Delete,
                title = stringResource(R.string.action_uninstall),
                color = DangerRed,
                onClick = {
                    onUninstall()
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CleanContextMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color = CleanTextPrimary,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanSettingsBottomSheet(
    uiState: LauncherUiState,
    onDismiss: () -> Unit,
    onToggleTextOnly: (Boolean) -> Unit,
    onToggleRamWidget: (Boolean) -> Unit,
    onToggleQuickLaunchBar: (Boolean) -> Unit,
    onToggleShowClock: (Boolean) -> Unit,
    onToggleDarkTheme: (Boolean) -> Unit,
    onToggleFrequentContacts: (Boolean) -> Unit,
    onRequestContactsPermission: () -> Unit,
    onOpenHomeSettings: () -> Unit,
    onManageQuickLaunch: () -> Unit,
    onManageHiddenApps: () -> Unit,
    onTrimRam: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = CleanSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = CleanTextPrimary
            )
            Text(
                text = "Clean Minimalism • Lightweight Launcher",
                style = MaterialTheme.typography.bodySmall,
                color = CleanGreenDark
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Set as default launcher card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CleanBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
                onClick = {
                    onOpenHomeSettings()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = CleanGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.set_as_default_launcher),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = CleanTextPrimary
                        )
                        Text(
                            text = stringResource(R.string.set_as_default_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pure Black AMOLED Dark Mode Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.theme_pure_black),
                description = stringResource(R.string.theme_pure_black_desc),
                checked = uiState.isDarkTheme,
                onCheckedChange = onToggleDarkTheme
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Frequent Contacts Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.frequent_contacts_title),
                description = stringResource(R.string.frequent_contacts_desc),
                checked = uiState.showFrequentContacts,
                onCheckedChange = onToggleFrequentContacts
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Quick-Launch Bar Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.quick_launch_bar_title),
                description = stringResource(R.string.quick_launch_bar_desc),
                checked = uiState.showQuickLaunchBar,
                onCheckedChange = onToggleQuickLaunchBar
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Minimalist RAM Widget Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.ram_widget_title),
                description = stringResource(R.string.ram_widget_desc),
                checked = uiState.showRamWidget,
                onCheckedChange = onToggleRamWidget
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Text Only Mode Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.text_only_mode),
                description = stringResource(R.string.text_only_mode_desc),
                checked = uiState.isTextOnlyMode,
                onCheckedChange = onToggleTextOnly
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Show Clock toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.show_clock),
                description = stringResource(R.string.show_clock_desc),
                checked = uiState.showClock,
                onCheckedChange = onToggleShowClock
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Manage Quick Launch Apps
            Surface(
                color = Color.Transparent,
                onClick = onManageQuickLaunch,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.customize_quick_launch),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = CleanTextPrimary
                        )
                        Text(
                            text = "${uiState.quickLaunchApps.size} apps pinned to quick bar",
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanTextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = CleanGreen
                    )
                }
            }

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Manage Hidden Apps
            Surface(
                color = Color.Transparent,
                onClick = onManageHiddenApps,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.hidden_apps_title),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = CleanTextPrimary
                        )
                        Text(
                            text = "${uiState.hiddenApps.size} apps currently hidden",
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanTextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = CleanTextSecondary
                    )
                }
            }

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Trim RAM action
            Surface(
                color = Color.Transparent,
                onClick = {
                    onTrimRam()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = CleanGreen
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.clean_ram_action),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = CleanGreen
                        )
                        Text(
                            text = stringResource(R.string.clean_ram_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanTextSecondary
                        )
                    }
                }
            }

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 12.dp))

            // Designer Attribution Card: "Designed by Behnam"
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CleanBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
                modifier = Modifier.fillMaxWidth().testTag("designer_branding_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PastelGreenBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "B",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CleanGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${stringResource(R.string.app_designer_label)} ",
                                style = MaterialTheme.typography.bodySmall,
                                color = CleanTextSecondary
                            )
                            Text(
                                text = stringResource(R.string.app_designer_name),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = CleanTextPrimary
                            )
                        }
                        Text(
                            text = stringResource(R.string.app_designer_badge),
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CleanSettingToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = CleanTextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = CleanTextSecondary
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CleanGreen,
                uncheckedThumbColor = CleanTextSecondary,
                uncheckedTrackColor = CleanSurfaceVariant
            )
        )
    }
}

@Composable
fun QuickLaunchCustomizationDialog(
    apps: List<AppItem>,
    onDismiss: () -> Unit,
    onToggleQuickLaunch: (AppItem) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CleanSurface,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = stringResource(R.string.customize_quick_launch),
                color = CleanTextPrimary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                items(apps, key = { it.packageName }) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleQuickLaunch(app) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CleanSquircleBadge(label = app.label, size = 36)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = app.label,
                                color = CleanTextPrimary,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Checkbox(
                            checked = app.isQuickLaunch,
                            onCheckedChange = { onToggleQuickLaunch(app) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = CleanGreen,
                                checkmarkColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Done", color = CleanGreen)
            }
        }
    )
}

@Composable
fun CleanHiddenAppsDialog(
    hiddenApps: List<AppItem>,
    onDismiss: () -> Unit,
    onUnhide: (AppItem) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CleanSurface,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = stringResource(R.string.hidden_apps_title),
                color = CleanTextPrimary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        text = {
            if (hiddenApps.isEmpty()) {
                Text(
                    text = "No hidden apps. You can hide apps from the drawer by long-pressing them.",
                    color = CleanTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(hiddenApps, key = { it.packageName }) { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CleanSquircleBadge(label = app.label, size = 36)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = app.label,
                                    color = CleanTextPrimary,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            TextButton(onClick = { onUnhide(app) }) {
                                Text(
                                    text = stringResource(R.string.action_unhide),
                                    color = CleanGreen
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close", color = CleanGreen)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CleanContactCard(
    contact: ContactItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = CleanSurface,
        modifier = modifier
            .width(88.dp)
            .height(104.dp)
            .border(1.dp, CleanBorder, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("contact_item_${contact.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PastelBlueBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = contact.displayName,
                    tint = PastelBlueText,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = contact.displayName,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
                color = CleanTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.call_contact),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = CleanGreen,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
