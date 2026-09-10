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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.FilterChip
import androidx.compose.ui.graphics.graphicsLayer
import com.example.data.AppSortOrder
import com.example.util.CpuStatus
import kotlin.math.absoluteValue
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.launch
import com.example.data.ContactItem
import com.example.util.PersianDateHelper
import com.example.util.GoogleWidgetHelper
import com.example.ui.theme.VazirFontFamily
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

    var pendingCallContact by remember { mutableStateOf<ContactItem?>(null) }
    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updatePermissions()
        pendingCallContact?.let { contact ->
            viewModel.callContact(contact)
            pendingCallContact = null
        }
    }

    val onContactClick: (ContactItem) -> Unit = { contact ->
        if (viewModel.hasCallPermission()) {
            viewModel.callContact(contact)
        } else {
            pendingCallContact = contact
            callPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbarMessage()
        }
    }

    val totalAppPages = uiState.appPages.size.coerceAtLeast(1)
    val pageCount = 1 + totalAppPages
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })
    val coroutineScope = rememberCoroutineScope()

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
            if (uiState.searchQuery.isNotBlank()) {
                // ==================== ACTIVE SEARCH VIEW ====================
                if (uiState.isPixelStyle) {
                    PixelSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onClearQuery = { viewModel.clearSearch() },
                        onSearchAction = {
                            if (uiState.filteredApps.isNotEmpty()) {
                                viewModel.launchTopSearchResult()
                            } else {
                                GoogleWidgetHelper.launchGoogleSearch(context, uiState.searchQuery)
                                viewModel.clearSearch()
                            }
                        },
                        onOpenSettings = { viewModel.toggleSettingsSheet(true) },
                        onGoogleGClick = {
                            GoogleWidgetHelper.launchGoogleSearch(context)
                        },
                        onVoiceSearchClick = {
                            GoogleWidgetHelper.launchVoiceSearch(context)
                        },
                        onLensClick = {
                            GoogleWidgetHelper.launchGoogleLens(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } else {
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
                }

                // Dedicated Google Web Search Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, CleanBorder),
                    onClick = {
                        GoogleWidgetHelper.launchGoogleSearch(context, uiState.searchQuery)
                        viewModel.clearSearch()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GoogleGLogo(size = 20.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.search_google_web, uiState.searchQuery),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = CleanTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.action_open_google),
                            tint = GoogleBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                CleanSectionHeader(
                    title = stringResource(R.string.all_apps_title),
                    count = uiState.filteredApps.size,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )

                if (uiState.filteredApps.isEmpty() && !uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
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
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("search_results_lazy_column"),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        items(
                            items = uiState.filteredApps,
                            key = { it.packageName }
                        ) { app ->
                            CleanAppRowItem(
                                app = app,
                                isTextOnly = uiState.isTextOnlyMode,
                                getBitmap = { viewModel.getBitmap(app.packageName) },
                                onClick = { viewModel.launchApp(app) },
                                onLongClick = { viewModel.selectAppForMenu(app) },
                                modifier = Modifier.testTag("app_item_${app.packageName}")
                            )
                        }
                    }
                }
            } else if (uiState.isPagedApps) {
                // ==================== HORIZONTAL PAGED MODE (Pixel / Modern) ====================
                if (uiState.isPixelStyle) {
                    PixelAtAGlanceWidget(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp)
                    )
                } else if (uiState.showClock) {
                    ClockAndDateHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    )
                }

                // Smooth fling and snap physics for effortless swiping
                val pagerFlingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapAnimationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )

                // Horizontal Pager: Page 0 = Home Dashboard; Page 1..N = Sliding App Pages
                HorizontalPager(
                    state = pagerState,
                    flingBehavior = pagerFlingBehavior,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("launcher_horizontal_pager")
                ) { page ->
                    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val scale = 1f - (pageOffset * 0.05f).coerceIn(0f, 0.1f)
                                scaleX = scale
                                scaleY = scale
                                alpha = (1f - (pageOffset * 0.35f)).coerceIn(0.5f, 1f)
                            }
                    ) {
                        if (page == 0) {
                            // Home Page
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(bottom = 8.dp)
                            ) {
                                // RAM Widget
                                if (uiState.showRamWidget) {
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

                                // CPU & Temperature Widget
                                if (uiState.showCpuWidget) {
                                    MinimalistCpuWidget(
                                        cpuStatus = uiState.cpuStatus,
                                        onRefresh = { viewModel.updateSystemStatus() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 4.dp)
                                    )
                                }

                            // Frequent Contacts
                            if (uiState.showFrequentContacts) {
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
                                                .padding(bottom = 8.dp)
                                                .testTag("frequent_contacts_row")
                                        ) {
                                            items(
                                                items = contacts,
                                                key = { c: ContactItem -> "contact_${c.id}" }
                                            ) { contact ->
                                                CleanContactCard(
                                                    contact = contact,
                                                    onClick = { onContactClick(contact) }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = CleanSurface,
                                        border = BorderStroke(1.dp, CleanBorder),
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
                                                tint = GoogleBlue,
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
                                                    color = GoogleBlue,
                                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Pinned Favorites Apps
                            if (uiState.pinnedApps.isNotEmpty()) {
                                CleanSectionHeader(
                                    title = stringResource(R.string.favorites_title),
                                    count = uiState.pinnedApps.size,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                        .testTag("pinned_apps_row")
                                ) {
                                    items(
                                        items = uiState.pinnedApps,
                                        key = { "pinned_${it.packageName}" }
                                    ) { app ->
                                        CleanPinnedAppCard(
                                            app = app,
                                            isTextOnly = uiState.isTextOnlyMode,
                                            getBitmap = { viewModel.getBitmap(app.packageName) },
                                            onClick = { viewModel.launchApp(app) },
                                            onLongClick = { viewModel.selectAppForMenu(app) }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Interactive Prompt: "Swipe left for Apps ›"
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (AppTheme.colors.isDark) PixelChipBgDark else PixelChipBgLight,
                                border = BorderStroke(1.dp, CleanBorder),
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(1)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 6.dp)
                                    .testTag("swipe_for_apps_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Apps,
                                            contentDescription = null,
                                            tint = GoogleBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = stringResource(R.string.swipe_for_apps_hint),
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                            color = CleanTextPrimary
                                        )
                                    }
                                    Text(
                                        text = "${uiState.apps.size} apps ›",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = GoogleBlue
                                    )
                                }
                            }
                        }
                    } else {
                        // App Page (Page 1..N)
                        val pageIndex = page - 1
                        val pageApps = uiState.appPages.getOrElse(pageIndex) { emptyList() }
                        PixelAppPage(
                            pageIndex = pageIndex,
                            totalPages = totalAppPages,
                            apps = pageApps,
                            isTextOnly = uiState.isTextOnlyMode,
                            getBitmap = { viewModel.getBitmap(it) },
                            onAppClick = { viewModel.launchApp(it) },
                            onAppLongClick = { viewModel.selectAppForMenu(it) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

                // Page Indicator Dots
                PixelPageIndicator(
                    pageCount = pageCount,
                    currentPage = pagerState.currentPage,
                    onPageSelected = { targetPage ->
                        coroutineScope.launch { pagerState.animateScrollToPage(targetPage) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                // Quick Launch Bar / Dock
                if (uiState.showQuickLaunchBar) {
                    CustomizableQuickLaunchBar(
                        quickApps = uiState.quickLaunchApps,
                        isTextOnly = uiState.isTextOnlyMode,
                        getBitmap = { viewModel.getBitmap(it) },
                        onAppClick = { viewModel.launchApp(it) },
                        onAppLongClick = { viewModel.selectAppForMenu(it) },
                        onOpenPicker = { viewModel.toggleQuickLaunchPicker(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 2.dp)
                    )
                }

                // Bottom Search Bar (Google Pixel Search Bar or Clean Bar)
                if (uiState.isPixelStyle) {
                    PixelSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onClearQuery = { viewModel.clearSearch() },
                        onSearchAction = {
                            if (uiState.filteredApps.isNotEmpty()) {
                                viewModel.launchTopSearchResult()
                            } else {
                                GoogleWidgetHelper.launchGoogleSearch(context, uiState.searchQuery)
                                viewModel.clearSearch()
                            }
                        },
                        onOpenSettings = { viewModel.toggleSettingsSheet(true) },
                        onGoogleGClick = {
                            GoogleWidgetHelper.launchGoogleSearch(context)
                        },
                        onVoiceSearchClick = {
                            GoogleWidgetHelper.launchVoiceSearch(context)
                        },
                        onLensClick = {
                            GoogleWidgetHelper.launchGoogleLens(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                } else {
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
                }
            } else {
                // ==================== CLASSIC SINGLE-PAGE DRAWER MODE ====================
                if (uiState.showClock) {
                    ClockAndDateHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                if (uiState.showRamWidget) {
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

                if (uiState.showQuickLaunchBar) {
                    CustomizableQuickLaunchBar(
                        quickApps = uiState.quickLaunchApps,
                        isTextOnly = uiState.isTextOnlyMode,
                        getBitmap = { viewModel.getBitmap(it) },
                        onAppClick = { viewModel.launchApp(it) },
                        onAppLongClick = { viewModel.selectAppForMenu(it) },
                        onOpenPicker = { viewModel.toggleQuickLaunchPicker(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }

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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("app_list_lazy_column"),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    // Pinned Apps
                    if (uiState.pinnedApps.isNotEmpty()) {
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
                                        getBitmap = { viewModel.getBitmap(app.packageName) },
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
                        CleanSectionHeader(
                            title = stringResource(R.string.all_apps_title),
                            count = uiState.filteredApps.size,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    // Drawer App Items
                    items(
                        items = uiState.filteredApps,
                        key = { it.packageName }
                    ) { app ->
                        CleanAppRowItem(
                            app = app,
                            isTextOnly = uiState.isTextOnlyMode,
                            getBitmap = { viewModel.getBitmap(app.packageName) },
                            onClick = { viewModel.launchApp(app) },
                            onLongClick = { viewModel.selectAppForMenu(app) },
                            modifier = Modifier.testTag("app_item_${app.packageName}")
                        )
                    }
                }

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
            onToggleHide = { viewModel.toggleHide(selectedApp) },
            onMoveUp = { viewModel.moveAppUp(selectedApp) },
            onMoveDown = { viewModel.moveAppDown(selectedApp) },
            onOpenReorder = {
                viewModel.selectAppForMenu(null)
                viewModel.toggleReorderSheet(true)
            }
        )
    }

    // Reorder Apps Bottom Sheet
    if (uiState.showReorderSheet) {
        ReorderAppsBottomSheet(
            uiState = uiState,
            onDismiss = { viewModel.toggleReorderSheet(false) },
            onSetSortOrder = { viewModel.setAppSortOrder(it) },
            onMoveUp = { viewModel.moveAppUp(it) },
            onMoveDown = { viewModel.moveAppDown(it) },
            onMoveToPage = { app, page -> viewModel.moveAppToPage(app, page) },
            onResetToDefault = { viewModel.resetAppOrderToDefault() }
        )
    }

    // Settings Bottom Sheet
    if (uiState.showSettingsSheet) {
        CleanSettingsBottomSheet(
            uiState = uiState,
            onDismiss = { viewModel.toggleSettingsSheet(false) },
            onToggleTextOnly = { viewModel.setTextOnlyMode(it) },
            onToggleRamWidget = { viewModel.setShowRamWidget(it) },
            onToggleCpuWidget = { viewModel.setShowCpuWidget(it) },
            onToggleQuickLaunchBar = { viewModel.setShowQuickLaunchBar(it) },
            onToggleShowClock = { viewModel.setShowClock(it) },
            onToggleDarkTheme = { viewModel.setDarkTheme(it) },
            onToggleDynamicColor = { viewModel.setDynamicColor(it) },
            onTogglePixelStyle = { viewModel.setPixelStyle(it) },
            onTogglePagedApps = { viewModel.setPagedApps(it) },
            onToggleNothingStyle = { viewModel.setNothingStyle(it) },
            onOpenReorderApps = {
                viewModel.toggleSettingsSheet(false)
                viewModel.toggleReorderSheet(true)
            },
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
            },
            onOpenAbout = {
                viewModel.toggleSettingsSheet(false)
                viewModel.toggleAboutDialog(true)
            }
        )
    }

    // Nothing OS About Overlay
    if (uiState.showAboutDialog) {
        NothingAboutOverlay(
            onDismiss = { viewModel.toggleAboutDialog(false) }
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
                val isDark = AppTheme.colors.isDark
                val badgeBg = if (isDark) CleanGreen.copy(alpha = 0.25f) else CleanGreen.copy(alpha = 0.12f)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(badgeBg),
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
 * Minimalist CPU & Temperature Monitor Widget
 * Displays live processor utilization, active core count, and hardware thermal temperature.
 */
@Composable
fun MinimalistCpuWidget(
    cpuStatus: CpuStatus,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = AppTheme.colors.isDark
    val loadColor = when {
        cpuStatus.usagePercent > 80 -> Color(0xFFEA4335)
        cpuStatus.usagePercent > 50 -> Color(0xFFFBBC04)
        else -> GoogleBlue
    }
    val badgeBg = loadColor.copy(alpha = if (isDark) 0.25f else 0.12f)

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = CleanSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
        modifier = modifier
            .testTag("minimalist_cpu_widget")
            .clip(RoundedCornerShape(18.dp))
            .clickable { onRefresh() }
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
                // CPU load badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(badgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${cpuStatus.usagePercent}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = loadColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.cpu_usage_label),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = CleanTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${cpuStatus.coreCount} Cores",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = CleanTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    LinearProgressIndicator(
                        progress = { (cpuStatus.usagePercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = loadColor,
                        trackColor = CleanSurfaceVariant
                    )
                }
            }

            // Temperature Readout
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = cpuStatus.tempFormatted,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (cpuStatus.tempCelsius > 45f) Color(0xFFEA4335) else CleanTextPrimary
                    )
                    Text(
                        text = if (cpuStatus.tempCelsius > 45f) stringResource(R.string.thermal_warm) else stringResource(R.string.thermal_normal),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (cpuStatus.tempCelsius > 45f) Color(0xFFEA4335) else CleanGreen
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.Default.DeviceThermostat,
                    contentDescription = stringResource(R.string.cpu_temp_label),
                    tint = if (cpuStatus.tempCelsius > 45f) Color(0xFFEA4335) else CleanTextSecondary,
                    modifier = Modifier.size(18.dp)
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
    getBitmap: (String) -> Bitmap?,
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
                            getBitmap = { getBitmap(app.packageName) },
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
    getBitmap: () -> Bitmap?,
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
            val bmp = remember(app.packageName) { getBitmap() }
            if (bmp != null) {
                FastAppIconImage(bitmap = bmp, modifier = Modifier.size(38.dp))
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
        Spacer(modifier = Modifier.height(4.dp))
        val gregorianDateStr = dateFormatter.format(currentTime)
        val persianDate = remember(currentTime) { PersianDateHelper.getPersianDate(currentTime) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = gregorianDateStr,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = CleanTextSecondary.copy(alpha = 0.85f)
            )
            Text(
                text = "•",
                style = MaterialTheme.typography.titleMedium,
                color = CleanTextMuted
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (AppTheme.colors.isDark) PixelChipBgDark else PixelChipBgLight,
                border = BorderStroke(0.5.dp, CleanBorder)
            ) {
                Text(
                    text = persianDate.formatShort(toPersianDigits = true),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = VazirFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = GoogleBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Text(
            text = persianDate.formatFull(toPersianDigits = true),
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = VazirFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp
            ),
            color = CleanTextSecondary.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 2.dp)
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
    getBitmap: () -> Bitmap?,
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
                val bmp = remember(app.packageName) { getBitmap() }
                if (bmp != null) {
                    FastAppIconImage(bitmap = bmp, modifier = Modifier.size(42.dp))
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
    getBitmap: () -> Bitmap?,
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
            val bmp = remember(app.packageName) { getBitmap() }
            if (bmp != null) {
                FastAppIconImage(bitmap = bmp, modifier = Modifier.size(48.dp))
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
    val isDark = AppTheme.colors.isDark
    val (baseBg, baseText) = cleanPastelPalettes[colorIndex]
    val bgColor = if (isDark) baseBg.copy(alpha = 0.22f) else baseBg
    val textColor = if (isDark) PureBlackTextPrimary else baseText

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
fun FastAppIconImage(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier
) {
    if (bitmap != null) {
        val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
        androidx.compose.foundation.Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = modifier.clip(RoundedCornerShape(14.dp))
        )
    }
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
    onToggleHide: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onOpenReorder: () -> Unit
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

            // Move Up
            CleanContextMenuItem(
                icon = Icons.Default.ArrowUpward,
                title = stringResource(R.string.action_move_up),
                color = CleanTextPrimary,
                onClick = {
                    onMoveUp()
                    onDismiss()
                }
            )

            // Move Down
            CleanContextMenuItem(
                icon = Icons.Default.ArrowDownward,
                title = stringResource(R.string.action_move_down),
                color = CleanTextPrimary,
                onClick = {
                    onMoveDown()
                    onDismiss()
                }
            )

            // Organize & Reorder All Apps
            CleanContextMenuItem(
                icon = Icons.Default.Reorder,
                title = stringResource(R.string.reorder_apps_title),
                color = GoogleBlue,
                onClick = {
                    onDismiss()
                    onOpenReorder()
                }
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 4.dp))

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
    color: Color? = null,
    onClick: () -> Unit
) {
    val resolvedColor = color ?: AppTheme.colors.textPrimary
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
                tint = resolvedColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = resolvedColor
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
    onToggleCpuWidget: (Boolean) -> Unit,
    onToggleQuickLaunchBar: (Boolean) -> Unit,
    onToggleShowClock: (Boolean) -> Unit,
    onToggleDarkTheme: (Boolean) -> Unit,
    onToggleDynamicColor: (Boolean) -> Unit,
    onTogglePixelStyle: (Boolean) -> Unit,
    onTogglePagedApps: (Boolean) -> Unit,
    onToggleNothingStyle: (Boolean) -> Unit,
    onOpenReorderApps: () -> Unit,
    onToggleFrequentContacts: (Boolean) -> Unit,
    onRequestContactsPermission: () -> Unit,
    onOpenHomeSettings: () -> Unit,
    onManageQuickLaunch: () -> Unit,
    onManageHiddenApps: () -> Unit,
    onTrimRam: () -> Unit,
    onOpenAbout: () -> Unit
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.isPixelStyle) {
                    GoogleGLogo(size = 28.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Column {
                    Text(
                        text = if (uiState.isPixelStyle) "Pixel Launcher Settings" else stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = CleanTextPrimary
                    )
                    Text(
                        text = if (uiState.isPixelStyle) "Material You • Google Pixel Edition" else "Clean Minimalism • Lightweight Launcher",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (uiState.isPixelStyle) GoogleBlue else CleanGreenDark
                    )
                }
            }

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
                        tint = if (uiState.isPixelStyle) GoogleBlue else CleanGreen,
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

            // Google Pixel Launcher Style Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.pixel_style_title),
                description = stringResource(R.string.pixel_style_desc),
                checked = uiState.isPixelStyle,
                onCheckedChange = onTogglePixelStyle
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Sliding App Pages Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.paged_apps_title),
                description = stringResource(R.string.paged_apps_desc),
                checked = uiState.isPagedApps,
                onCheckedChange = onTogglePagedApps
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Material You Dynamic Color Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.dynamic_color_title),
                description = stringResource(R.string.dynamic_color_desc),
                checked = uiState.isDynamicColor,
                onCheckedChange = onToggleDynamicColor
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Pure Black AMOLED Dark Mode Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.theme_pure_black),
                description = stringResource(R.string.theme_pure_black_desc),
                checked = uiState.isDarkTheme,
                onCheckedChange = onToggleDarkTheme
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Nothing OS Aesthetic Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.nothing_style_title),
                description = stringResource(R.string.nothing_style_desc),
                checked = uiState.isNothingStyle,
                onCheckedChange = onToggleNothingStyle
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

            // Minimalist CPU & Temperature Widget Toggle
            CleanSettingToggleItem(
                title = stringResource(R.string.cpu_widget_title),
                description = stringResource(R.string.cpu_widget_desc),
                checked = uiState.showCpuWidget,
                onCheckedChange = onToggleCpuWidget
            )

            HorizontalDivider(color = CleanBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Organize & Reorder Apps Button
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CleanBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
                onClick = onOpenReorderApps,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Reorder,
                        contentDescription = null,
                        tint = GoogleBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.reorder_apps_title),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = CleanTextPrimary
                        )
                        Text(
                            text = stringResource(R.string.reorder_apps_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanTextSecondary
                        )
                    }
                }
            }

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

            // Designer Attribution Card: "Designed by Behnam" -> opens Nothing OS About Overlay
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CleanBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
                onClick = onOpenAbout,
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
                    Column(modifier = Modifier.weight(1f)) {
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
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(R.string.about_title),
                        tint = CleanTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
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
            val isDark = AppTheme.colors.isDark
            val iconBg = if (isDark) PastelBlueBg.copy(alpha = 0.22f) else PastelBlueBg
            val iconTint = if (isDark) PureBlackTextPrimary else PastelBlueText

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = contact.displayName,
                    tint = iconTint,
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

/**
 * About Overlay Component styled in the minimalist Nothing OS aesthetic.
 * Features dot-matrix typography, monochrome surfaces, Nothing Red accents,
 * application metadata, and designer attribution.
 */
@Composable
fun NothingAboutOverlay(
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = PureBlackSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, PureBlackBorder),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null,
                        onClick = {} // prevent dismissing when tapping card
                    )
                    .testTag("about_overlay_card")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Nothing OS Brand Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NothingRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.nothing_glyph_brand),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            ),
                            color = NothingRed
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = stringResource(R.string.about_title),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Light,
                            letterSpacing = (-0.5).sp,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = PureBlackTextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.about_app_version),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        ),
                        color = PureBlackTextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Minimal metadata description container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(PureBlackSurfaceVariant)
                            .border(1.dp, PureBlackBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about_app_tagline),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                fontSize = 13.sp
                            ),
                            color = PureBlackTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Designer & Developer Attribution Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(PureBlackSurfaceVariant)
                            .border(1.dp, PureBlackBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(NothingRed.copy(alpha = 0.2f))
                                    .border(1.dp, NothingRed.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "B",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = NothingRed
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.about_dev_label),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = PureBlackTextMuted
                                )
                                Text(
                                    text = stringResource(R.string.app_designer_name),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = PureBlackTextPrimary
                                )
                                Text(
                                    text = stringResource(R.string.app_designer_badge),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = NothingRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Close Button
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PureBlackSurfaceVariant,
                            contentColor = PureBlackTextPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PureBlackBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("about_overlay_close_button")
                    ) {
                        Text(
                            text = stringResource(R.string.close_button),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Google Pixel Signature Components
// -------------------------------------------------------------

@Composable
fun GoogleGLogo(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 22.dp) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = this.size.minDimension * 0.22f
        val radius = (this.size.minDimension - strokeWidth) / 2f
        val center = this.center

        val arcRect = Rect(
            left = center.x - radius,
            top = center.y - radius,
            right = center.x + radius,
            bottom = center.y + radius
        )

        // Red (top)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 195f,
            sweepAngle = 110f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Yellow (left)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 135f,
            sweepAngle = 65f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Green (bottom)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 35f,
            sweepAngle = 105f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Blue (right arc)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -25f,
            sweepAngle = 65f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Blue horizontal crossbar into center
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(center.x - strokeWidth * 0.1f, center.y),
            end = Offset(center.x + radius + strokeWidth / 2f, center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )
    }
}

@Composable
fun PixelAtAGlanceWidget(
    modifier: Modifier = Modifier,
    onDateClick: () -> Unit = {},
    onWeatherClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var currentTime by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(30000)
        }
    }

    val gregorianDateText = remember(currentTime) {
        val sdf = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        sdf.format(currentTime)
    }
    val persianDate = remember(currentTime) { PersianDateHelper.getPersianDate(currentTime) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
    ) {
        // Date row - clicking opens Google / System Calendar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    onDateClick()
                    GoogleWidgetHelper.launchCalendar(context)
                }
                .padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            Text(
                text = gregorianDateText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    letterSpacing = (-0.2).sp
                ),
                color = CleanTextPrimary
            )
            Text(
                text = "•",
                style = MaterialTheme.typography.titleMedium,
                color = CleanTextMuted
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (AppTheme.colors.isDark) PixelChipBgDark else PixelChipBgLight,
                border = BorderStroke(0.5.dp, CleanBorder)
            ) {
                Text(
                    text = persianDate.formatShort(toPersianDigits = true),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = VazirFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Weather row - clicking opens Google Weather
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onWeatherClick()
                    GoogleWidgetHelper.launchWeather(context)
                }
                .padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = stringResource(R.string.action_weather),
                tint = GoogleYellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.pixel_weather_sample),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                color = CleanTextSecondary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = persianDate.formatFull(toPersianDigits = true),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = VazirFontFamily,
                    fontSize = 12.sp
                ),
                color = CleanTextMuted
            )
        }
    }
}

@Composable
fun PixelSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSearchAction: () -> Unit,
    onOpenSettings: () -> Unit,
    onGoogleGClick: () -> Unit = {},
    onVoiceSearchClick: () -> Unit = {},
    onLensClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = AppTheme.colors.isDark
    val bgColor = if (isDark) PixelSearchPillDark else PixelSearchPillLight
    val borderColor = if (isDark) Color(0xFF333538) else Color(0xFFDFE2E6)

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = if (isDark) 0.dp else 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Google G Button (Launches Google App or Search)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onGoogleGClick),
                contentAlignment = Alignment.Center
            ) {
                GoogleGLogo(size = 22.dp)
            }

            Spacer(modifier = Modifier.width(6.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("pixel_search_input"),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = CleanTextPrimary,
                    fontWeight = FontWeight.Normal
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchAction() }),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_pixel_hint),
                            style = MaterialTheme.typography.bodyLarge,
                            color = CleanTextMuted
                        )
                    }
                    innerTextField()
                }
            )

            if (query.isNotEmpty()) {
                IconButton(
                    onClick = onClearQuery,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = CleanTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = onVoiceSearchClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = stringResource(R.string.action_voice_search),
                        tint = GoogleBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onLensClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CenterFocusWeak,
                        contentDescription = stringResource(R.string.action_google_lens),
                        tint = GoogleRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier.size(36.dp).testTag("pixel_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings_title),
                    tint = CleanTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PixelGridAppItem(
    app: AppItem,
    isTextOnly: Boolean,
    getBitmap: () -> Bitmap?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isTextOnly) {
            CleanSquircleBadge(label = app.label, size = 48)
        } else {
            val bmp = remember(app.packageName) { getBitmap() }
            if (bmp != null) {
                FastAppIconImage(
                    bitmap = bmp,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            } else {
                CleanSquircleBadge(label = app.label, size = 48)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = app.label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            ),
            color = CleanTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PixelPageIndicator(
    pageCount: Int,
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (pageCount <= 1) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (page in 0 until pageCount) {
            val isSelected = page == currentPage
            val width = if (isSelected) 22.dp else 7.dp
            val color = if (isSelected) GoogleBlue else CleanBorder

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .height(7.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onPageSelected(page) }
            )
        }
    }
}

@Composable
fun PixelAppPage(
    pageIndex: Int,
    totalPages: Int,
    apps: List<AppItem>,
    isTextOnly: Boolean,
    getBitmap: (String) -> Bitmap?,
    onAppClick: (AppItem) -> Unit,
    onAppLongClick: (AppItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.all_apps_title),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = CleanTextSecondary
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PixelChipBgLight.copy(alpha = 0.8f))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${pageIndex + 1} / $totalPages",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = CleanTextPrimary
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 8.dp)
        ) {
            items(
                items = apps,
                key = { it.packageName }
            ) { app ->
                PixelGridAppItem(
                    app = app,
                    isTextOnly = isTextOnly,
                    getBitmap = { getBitmap(app.packageName) },
                    onClick = { onAppClick(app) },
                    onLongClick = { onAppLongClick(app) },
                    modifier = Modifier.testTag("paged_app_${app.packageName}")
                )
            }
        }
    }
}

/**
 * Reorder & Organize Apps Bottom Sheet
 * Allows selecting sorting algorithms (A-Z, Z-A, Custom) or moving individual apps up and down.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderAppsBottomSheet(
    uiState: LauncherUiState,
    onDismiss: () -> Unit,
    onSetSortOrder: (AppSortOrder) -> Unit,
    onMoveUp: (AppItem) -> Unit,
    onMoveDown: (AppItem) -> Unit,
    onMoveToPage: (AppItem, Int) -> Unit,
    onResetToDefault: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val displayApps = remember(uiState.apps, searchQuery) {
        val active = uiState.apps.filter { !it.isHidden }
        if (searchQuery.isBlank()) active
        else active.filter { it.label.contains(searchQuery, ignoreCase = true) }
    }
    val totalPages = remember(uiState.apps) {
        val nonHidden = uiState.apps.filter { !it.isHidden }
        ((nonHidden.size + 19) / 20).coerceAtLeast(1)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = CleanSurface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Reorder,
                        contentDescription = null,
                        tint = GoogleBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.reorder_apps_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CleanTextPrimary
                        )
                        Text(
                            text = "${displayApps.size} apps • $totalPages pages",
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanTextSecondary
                        )
                    }
                }

                TextButton(onClick = onResetToDefault) {
                    Text(
                        text = stringResource(R.string.reset_order),
                        style = MaterialTheme.typography.labelMedium,
                        color = GoogleBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sort Mode Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.appSortOrder == AppSortOrder.ALPHABETICAL_ASC,
                    onClick = { onSetSortOrder(AppSortOrder.ALPHABETICAL_ASC) },
                    label = { Text(stringResource(R.string.sort_alphabetical_asc)) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = uiState.appSortOrder == AppSortOrder.ALPHABETICAL_DESC,
                    onClick = { onSetSortOrder(AppSortOrder.ALPHABETICAL_DESC) },
                    label = { Text(stringResource(R.string.sort_alphabetical_desc)) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = uiState.appSortOrder == AppSortOrder.CUSTOM,
                    onClick = { onSetSortOrder(AppSortOrder.CUSTOM) },
                    label = { Text(stringResource(R.string.sort_custom)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // App list with Up/Down buttons
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(
                    items = displayApps,
                    key = { it.packageName }
                ) { app ->
                    val indexInTotal = uiState.apps.indexOfFirst { it.packageName == app.packageName }
                    val pageNumber = if (indexInTotal >= 0) (indexInTotal / 20) + 1 else 1

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CleanBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CleanBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CleanSquircleBadge(label = app.label, size = 36)
                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = app.label,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = CleanTextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = stringResource(R.string.pixel_page_format, pageNumber) + " • #${indexInTotal + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CleanTextSecondary
                                )
                            }

                            // Up Button
                            IconButton(
                                onClick = { onMoveUp(app) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = stringResource(R.string.action_move_up),
                                    tint = CleanTextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Down Button
                            IconButton(
                                onClick = { onMoveDown(app) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = stringResource(R.string.action_move_down),
                                    tint = CleanTextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


