package com.example.util

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.provider.MediaStore
import android.speech.RecognizerIntent

/**
 * Helper to handle interactive actions for the Google Pixel widgets:
 * - Google Search pill (Search bar, Google 'G' icon, Voice search, Google Lens)
 * - Pixel At a Glance widget (Gregorian / Persian Date -> Calendar, Weather -> Google Weather)
 */
object GoogleWidgetHelper {

    /**
     * Opens Google search for the query, or launches the Google App if query is empty.
     */
    fun launchGoogleSearch(context: Context, query: String = "") {
        try {
            if (query.isNotBlank()) {
                val searchIntent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                    putExtra(SearchManager.QUERY, query)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(searchIntent)
            } else {
                val googleAppIntent = context.packageManager.getLaunchIntentForPackage("com.google.android.googlequicksearchbox")
                if (googleAppIntent != null) {
                    googleAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(googleAppIntent)
                } else {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(browserIntent)
                }
            }
        } catch (e: Exception) {
            try {
                val fallbackIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        if (query.isNotBlank()) "https://www.google.com/search?q=${Uri.encode(query)}"
                        else "https://www.google.com"
                    )
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            } catch (ignored: Exception) {}
        }
    }

    /**
     * Launches Google Voice Search / Speech Recognition.
     */
    fun launchVoiceSearch(context: Context) {
        try {
            val intent = Intent(RecognizerIntent.ACTION_WEB_SEARCH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val voiceIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(voiceIntent)
            } catch (e2: Exception) {
                launchGoogleSearch(context)
            }
        }
    }

    /**
     * Launches Google Lens app, camera, or lens web portal.
     */
    fun launchGoogleLens(context: Context) {
        try {
            val lensIntent = context.packageManager.getLaunchIntentForPackage("com.google.ar.lens")
            if (lensIntent != null) {
                lensIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(lensIntent)
                return
            }

            val lensUriIntent = Intent(Intent.ACTION_VIEW, Uri.parse("googleapp://lens")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(lensUriIntent)
        } catch (e: Exception) {
            try {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(cameraIntent)
            } catch (e2: Exception) {
                try {
                    val webLensIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://lens.google.com")).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(webLensIntent)
                } catch (ignored: Exception) {}
            }
        }
    }

    /**
     * Opens system or Google Calendar, with fallback to system clock/alarm.
     */
    fun launchCalendar(context: Context) {
        try {
            val calendarIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_APP_CALENDAR)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(calendarIntent)
        } catch (e: Exception) {
            try {
                val builder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time")
                val viewIntent = Intent(Intent.ACTION_VIEW, builder.build()).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(viewIntent)
            } catch (e2: Exception) {
                try {
                    val clockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(clockIntent)
                } catch (ignored: Exception) {}
            }
        }
    }

    /**
     * Opens Google Weather forecast directly.
     */
    fun launchWeather(context: Context) {
        try {
            val weatherIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=weather")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(weatherIntent)
        } catch (ignored: Exception) {}
    }
}
