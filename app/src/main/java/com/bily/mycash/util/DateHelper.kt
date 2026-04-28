package com.bily.mycash.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {

    private val locale = Locale("id", "ID")

    /**
     * Format epoch millis ke "23 Apr 2026"
     */
    fun formatDate(millis: Long): String {
        return SimpleDateFormat("dd MMM yyyy", locale).format(Date(millis))
    }

    /**
     * Format epoch millis ke "23 Apr"
     */
    fun formatShortDate(millis: Long): String {
        return SimpleDateFormat("dd MMM", locale).format(Date(millis))
    }

    /**
     * Format epoch millis ke "Apr 2026"
     */
    fun formatMonthYear(millis: Long): String {
        return SimpleDateFormat("MMM yyyy", locale).format(Date(millis))
    }

    /**
     * Format epoch millis ke "April 2026"
     */
    fun formatFullMonthYear(millis: Long): String {
        return SimpleDateFormat("MMMM yyyy", locale).format(Date(millis))
    }

    /**
     * Mendapatkan epoch millis hari ini (00:00)
     */
    fun todayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * Mendapatkan epoch millis awal bulan ini
     */
    fun startOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * Mendapatkan epoch millis akhir bulan ini
     */
    fun endOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    /**
     * Mendapatkan epoch millis N bulan yang lalu (awal bulan)
     */
    fun monthsAgo(months: Int): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -months)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * Mendapatkan nama bulan dari index (0-based): "Jan", "Feb", ...
     */
    fun getMonthName(monthIndex: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, monthIndex)
        return SimpleDateFormat("MMM", locale).format(cal.time)
    }
}
