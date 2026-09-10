package com.example.data

import android.app.Activity
import android.app.ActivityManager
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.util.LruCache
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LauncherRepository(private val context: Context) {
    private val prefs = LauncherPreferences(context)

    // Ultra-lightweight pre-rendered Bitmap cache: fast 60fps scrolling without re-allocating Bitmaps
    private val bitmapCache = object : LruCache<String, Bitmap>(64) {}

    val preferences: LauncherPreferences get() = prefs

    suspend fun getInstalledApps(): List<AppItem> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfos: List<ResolveInfo> = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(
                    mainIntent,
                    PackageManager.ResolveInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(mainIntent, 0)
            }
        } catch (e: Exception) {
            emptyList()
        }

        val pinnedSet = prefs.getPinnedPackages()
        val quickLaunchList = prefs.getQuickLaunchPackages()
        val quickLaunchSet = quickLaunchList.toSet()
        val hiddenSet = prefs.getHiddenPackages()
        val myPackage = context.packageName

        val appList = mutableListOf<AppItem>()
        for (info in resolveInfos) {
            val pkg = info.activityInfo.packageName
            if (pkg == myPackage) continue

            val label = try {
                info.loadLabel(packageManager).toString()
            } catch (e: Exception) {
                info.activityInfo.name
            }

            appList.add(
                AppItem(
                    label = label,
                    packageName = pkg,
                    activityName = info.activityInfo.name,
                    isPinned = pinnedSet.contains(pkg),
                    isQuickLaunch = quickLaunchSet.contains(pkg),
                    isHidden = hiddenSet.contains(pkg)
                )
            )
        }

        // Sort according to user preference
        val sortOrder = prefs.getAppSortOrder()
        when (sortOrder) {
            AppSortOrder.ALPHABETICAL_ASC -> appList.sortedBy { it.label.lowercase() }
            AppSortOrder.ALPHABETICAL_DESC -> appList.sortedByDescending { it.label.lowercase() }
            AppSortOrder.CUSTOM -> {
                val customOrder = prefs.getCustomAppOrder()
                if (customOrder.isEmpty()) {
                    appList.sortedBy { it.label.lowercase() }
                } else {
                    val orderMap = customOrder.withIndex().associate { it.value to it.index }
                    appList.sortedWith(compareBy(
                        { orderMap[it.packageName] ?: Int.MAX_VALUE },
                        { it.label.lowercase() }
                    ))
                }
            }
        }
    }

    fun getCpuStatus(): com.example.util.CpuStatus {
        return com.example.util.CpuMonitorHelper.getCpuStatus(context)
    }

    fun getAppBitmap(packageName: String): Bitmap? {
        bitmapCache.get(packageName)?.let { return it }

        return try {
            val drawable = context.packageManager.getApplicationIcon(packageName)
            val bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                else -> {
                    val w = drawable.intrinsicWidth.takeIf { it > 0 } ?: 96
                    val h = drawable.intrinsicHeight.takeIf { it > 0 } ?: 96
                    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bmp)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bmp
                }
            }
            bitmapCache.put(packageName, bitmap)
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    // Retained for backward compatibility
    fun getAppIcon(packageName: String): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }

    fun clearIconCache() {
        bitmapCache.evictAll()
        System.gc()
    }

    fun hasContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun getFrequentContacts(): List<ContactItem> = withContext(Dispatchers.IO) {
        if (!hasContactsPermission()) {
            return@withContext emptyList()
        }

        val contactsList = mutableListOf<ContactItem>()
        val contentResolver = context.contentResolver

        // Query frequent / starred contacts or contacts sorted by TIMES_CONTACTED descending
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.TIMES_CONTACTED,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )

        // Query contacts with phone numbers, sorted by times contacted, then starred
        val sortOrder = "${ContactsContract.Contacts.TIMES_CONTACTED} DESC, ${ContactsContract.Contacts.STARRED} DESC LIMIT 15"
        val cursor: Cursor? = try {
            contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                "${ContactsContract.Contacts.HAS_PHONE_NUMBER} = 1",
                null,
                sortOrder
            )
        } catch (e: Exception) {
            null
        }

        cursor?.use { c ->
            val idIdx = c.getColumnIndex(ContactsContract.Contacts._ID)
            val lookupIdx = c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            val nameIdx = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val timesIdx = c.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED)
            val photoIdx = c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

            while (c.moveToNext() && contactsList.size < 8) {
                val id = if (idIdx != -1) c.getString(idIdx) else ""
                val lookup = if (lookupIdx != -1) c.getString(lookupIdx) else ""
                val name = if (nameIdx != -1) c.getString(nameIdx) ?: "Contact" else "Contact"
                val times = if (timesIdx != -1) c.getInt(timesIdx) else 0
                val photoUri = if (photoIdx != -1) c.getString(photoIdx) else null

                // Query phone number for this contact ID
                var phone = ""
                val phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(id),
                    null
                )
                phoneCursor?.use { pc ->
                    if (pc.moveToFirst()) {
                        val numIdx = pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        if (numIdx != -1) {
                            phone = pc.getString(numIdx) ?: ""
                        }
                    }
                }

                if (phone.isNotBlank()) {
                    contactsList.add(
                        ContactItem(
                            id = id,
                            lookupKey = lookup,
                            displayName = name,
                            phoneNumber = phone,
                            timesContacted = times,
                            photoUri = photoUri
                        )
                    )
                }
            }
        }

        contactsList
    }

    fun hasCallPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun callContact(phoneNumber: String) {
        try {
            val action = if (hasCallPermission()) {
                Intent.ACTION_CALL
            } else {
                Intent.ACTION_DIAL
            }
            val intent = Intent(action).apply {
                data = Uri.parse("tel:${Uri.encode(phoneNumber)}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                // Fallback to dial if ACTION_CALL encounters any security issue
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${Uri.encode(phoneNumber)}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(dialIntent)
            } catch (_: Exception) {}
        }
    }

    fun getRamStatus(): RamStatus {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            ?: return RamStatus()
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        return RamStatus(
            totalBytes = mi.totalMem,
            availBytes = mi.availMem,
            isLowMemory = mi.lowMemory,
            thresholdBytes = mi.threshold
        )
    }

    fun launchApp(packageName: String, activityName: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                component = ComponentName(packageName, activityName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            // Fallback to launch intent for package
            try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    true
                } else {
                    false
                }
            } catch (e2: Exception) {
                false
            }
        }
    }

    fun openAppInfo(packageName: String) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun uninstallApp(packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun openHomeSettings(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = activity.getSystemService(RoleManager::class.java)
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                activity.startActivity(intent)
                return
            }
        }

        // Fallback for older versions or if roleManager is unavailable
        try {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            try {
                val intent = Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                activity.startActivity(intent)
            } catch (_: Exception) {}
        }
    }
}
