package com.example.util

import android.os.Build
import java.util.Calendar
import java.util.Date

data class PersianDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val dayOfWeekName: String,
    val monthName: String
) {
    /**
     * E.g. "چهارشنبه، ۱۸ شهریور ۱۴۰۵"
     */
    fun formatFull(toPersianDigits: Boolean = true): String {
        val y = if (toPersianDigits) year.toPersianDigits() else year.toString()
        val d = if (toPersianDigits) day.toPersianDigits() else day.toString()
        return "$dayOfWeekName، $d $monthName $y"
    }

    /**
     * E.g. "۱۸ شهریور"
     */
    fun formatShort(toPersianDigits: Boolean = true): String {
        val d = if (toPersianDigits) day.toPersianDigits() else day.toString()
        return "$d $monthName"
    }

    /**
     * E.g. "چهارشنبه، ۱۸ شهریور"
     */
    fun formatMedium(toPersianDigits: Boolean = true): String {
        val d = if (toPersianDigits) day.toPersianDigits() else day.toString()
        return "$dayOfWeekName، $d $monthName"
    }
}

object PersianDateHelper {

    private val PERSIAN_MONTH_NAMES = arrayOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )

    private val PERSIAN_DAY_OF_WEEK_NAMES = mapOf(
        Calendar.SATURDAY to "شنبه",
        Calendar.SUNDAY to "یکشنبه",
        Calendar.MONDAY to "دوشنبه",
        Calendar.TUESDAY to "سه‌شنبه",
        Calendar.WEDNESDAY to "چهارشنبه",
        Calendar.THURSDAY to "پنج‌شنبه",
        Calendar.FRIDAY to "جمعه"
    )

    fun getPersianDate(date: Date = Date()): PersianDate {
        // Try android.icu for high-fidelity regional accuracy if available on Android 7.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val uLocale = android.icu.util.ULocale("fa_IR@calendar=persian")
                val icuCal = android.icu.util.Calendar.getInstance(uLocale)
                icuCal.time = date
                val py = icuCal.get(android.icu.util.Calendar.YEAR)
                val pm = icuCal.get(android.icu.util.Calendar.MONTH) // 0-based
                val pd = icuCal.get(android.icu.util.Calendar.DATE)
                val dayOfWeek = icuCal.get(android.icu.util.Calendar.DAY_OF_WEEK)

                val monthName = PERSIAN_MONTH_NAMES.getOrElse(pm) { "شهریور" }
                val dayOfWeekName = PERSIAN_DAY_OF_WEEK_NAMES[dayOfWeek] ?: "چهارشنبه"

                return PersianDate(
                    year = py,
                    month = pm + 1,
                    day = pd,
                    dayOfWeekName = dayOfWeekName,
                    monthName = monthName
                )
            } catch (ignored: Throwable) {
                // Fallback to pure calculation below
            }
        }

        // Standard astronomical Jalali calculation fallback
        val cal = Calendar.getInstance()
        cal.time = date
        val gy = cal.get(Calendar.YEAR)
        val gm = cal.get(Calendar.MONTH) + 1
        val gd = cal.get(Calendar.DAY_OF_MONTH)
        val dow = cal.get(Calendar.DAY_OF_WEEK)

        val (jy, jm, jd) = gregorianToJalali(gy, gm, gd)
        val monthName = PERSIAN_MONTH_NAMES.getOrElse(jm - 1) { "شهریور" }
        val dayOfWeekName = PERSIAN_DAY_OF_WEEK_NAMES[dow] ?: "چهارشنبه"

        return PersianDate(
            year = jy,
            month = jm,
            day = jd,
            dayOfWeekName = dayOfWeekName,
            monthName = monthName
        )
    }

    private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): Triple<Int, Int, Int> {
        val gdm = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2 = if (gm > 2) gy - 1600 else gy - 1601
        var days = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400 - 80 + gd + gdm[gm - 1]
        var jy = 979 + 33 * (days / 12053)
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }
        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + days / 31
            jd = 1 + (days % 31)
        } else {
            jm = 7 + (days - 186) / 30
            jd = 1 + ((days - 186) % 30)
        }
        return Triple(jy, jm, jd)
    }
}

fun Int.toPersianDigits(): String = this.toString().toPersianDigits()

fun String.toPersianDigits(): String {
    val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val sb = StringBuilder()
    for (ch in this) {
        if (ch in '0'..'9') {
            sb.append(persianDigits[ch - '0'])
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}
