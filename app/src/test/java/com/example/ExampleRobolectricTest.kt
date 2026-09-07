package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppItem
import com.example.data.LauncherPreferences
import com.example.data.RamStatus
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
}
