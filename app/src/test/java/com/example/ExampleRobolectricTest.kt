package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppItem
import com.example.data.LauncherPreferences
import com.example.data.RamStatus
import com.example.util.PersianDateHelper
import com.example.util.toPersianDigits
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Lite Launcher", appName)
  }

  @Test
  fun `ramStatus calculation and formatting`() {
    val total = 8L * 1024L * 1024L * 1024L // 8GB
    val avail = 4L * 1024L * 1024L * 1024L // 4GB
    val ram = RamStatus(totalBytes = total, availBytes = avail)

    assertEquals(50, ram.usedPercent)
    assertEquals(50, ram.freePercent)
    assertTrue(ram.formattedTotal().contains("8.0 GB"))
    assertTrue(ram.formattedAvail().contains("4.0 GB"))
  }

  @Test
  fun `quick launch preferences persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = LauncherPreferences(context)

    prefs.setQuickLaunchPackages(listOf("com.android.dialer", "com.android.mms"))
    val result = prefs.getQuickLaunchPackages()

    assertEquals(2, result.size)
    assertTrue(result.contains("com.android.dialer"))
    assertTrue(result.contains("com.android.mms"))
  }

  @Test
  fun `quick launch state on app item`() {
    val app = AppItem(
      label = "Phone",
      packageName = "com.android.dialer",
      activityName = "com.android.dialer.DialtactsActivity",
      isQuickLaunch = true
    )

    assertTrue(app.isQuickLaunch)
    assertFalse(app.isPinned)
  }

  @Test
  fun `pixel style and paged apps preferences persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = LauncherPreferences(context)

    // Defaults should be true
    assertTrue(prefs.isPixelStyle())
    assertTrue(prefs.isPagedApps())

    // Toggle off
    prefs.setPixelStyle(false)
    prefs.setPagedApps(false)
    assertFalse(prefs.isPixelStyle())
    assertFalse(prefs.isPagedApps())

    // Toggle back on
    prefs.setPixelStyle(true)
    prefs.setPagedApps(true)
    assertTrue(prefs.isPixelStyle())
    assertTrue(prefs.isPagedApps())
  }

  @Test
  fun `persian date calculation and formatting`() {
    val cal = java.util.Calendar.getInstance()
    cal.set(2026, java.util.Calendar.SEPTEMBER, 9, 12, 0)
    val persianDate = PersianDateHelper.getPersianDate(cal.time)

    assertEquals(1405, persianDate.year)
    assertEquals(6, persianDate.month) // Shahrivar
    assertEquals("شهریور", persianDate.monthName)
    assertTrue(persianDate.day in 18..19)

    val shortStr = persianDate.formatShort(toPersianDigits = true)
    assertTrue(shortStr.contains("شهریور"))

    val fullStr = persianDate.formatFull(toPersianDigits = true)
    assertTrue(fullStr.contains("شهریور"))
    assertTrue(fullStr.contains("۱۴۰۵"))

    val testDigits = "1405/06/18".toPersianDigits()
    assertEquals("۱۴۰۵/۰۶/۱۸", testDigits)
  }
}
