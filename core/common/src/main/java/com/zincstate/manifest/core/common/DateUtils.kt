package com.zincstate.manifest.core.common

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "2026-09-17"
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    fun today(): String = LocalDate.now().format(dateFormatter)

    fun currentYearMonth(): String = YearMonth.now().format(yearMonthFormatter)

    fun formatDate(isoDate: String): String {
        return try {
            val date = LocalDate.parse(isoDate, dateFormatter)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val day = date.dayOfMonth
            val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            "$dayOfWeek, $day $month"
        } catch (e: Exception) {
            isoDate
        }
    }

    fun formatMonthYear(yearMonth: String): String {
        return try {
            val ym = YearMonth.parse(yearMonth, yearMonthFormatter)
            val month = ym.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val year = ym.year
            "$month $year"
        } catch (e: Exception) {
            yearMonth
        }
    }

    fun previousMonth(yearMonth: String): String {
        return try {
            val ym = YearMonth.parse(yearMonth, yearMonthFormatter)
            ym.minusMonths(1).format(yearMonthFormatter)
        } catch (e: Exception) {
            yearMonth
        }
    }

    fun nextMonth(yearMonth: String): String {
        return try {
            val ym = YearMonth.parse(yearMonth, yearMonthFormatter)
            ym.plusMonths(1).format(yearMonthFormatter)
        } catch (e: Exception) {
            yearMonth
        }
    }

    fun getDaysInMonth(yearMonth: String): Int {
        return try {
            YearMonth.parse(yearMonth, yearMonthFormatter).lengthOfMonth()
        } catch (e: Exception) {
            30
        }
    }

    fun getFirstDayOfWeek(yearMonth: String): Int {
        return try {
            val ym = YearMonth.parse(yearMonth, yearMonthFormatter)
            ym.atDay(1).dayOfWeek.value // 1=Monday, 7=Sunday
        } catch (e: Exception) {
            1
        }
    }

    fun daysAgo(days: Int): String {
        return LocalDate.now().minusDays(days.toLong()).format(dateFormatter)
    }

    fun getDayOfMonth(isoDate: String): Int {
        return try {
            LocalDate.parse(isoDate, dateFormatter).dayOfMonth
        } catch (e: Exception) {
            1
        }
    }

    fun yearMonthFromDate(isoDate: String): String {
        return try {
            isoDate.substring(0, 7) // "2026-09-17" → "2026-09"
        } catch (e: Exception) {
            currentYearMonth()
        }
    }
}
