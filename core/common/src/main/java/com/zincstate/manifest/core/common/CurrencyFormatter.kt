package com.zincstate.manifest.core.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

/**
 * Formats amounts using the Indian numbering system: ₹1,23,456.00
 * 
 * Grouping rules:
 * - Numbers < 1,000: plain (e.g., ₹999)
 * - 1,000–99,999: X,XXX (e.g., ₹12,345)
 * - ≥ 1,00,000: X,XX,XXX (e.g., ₹1,23,456)
 */
object CurrencyFormatter {

    private val symbols = DecimalFormatSymbols(Locale("en", "IN")).apply {
        currencySymbol = "₹"
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    /**
     * Format amount with Indian numbering system.
     * @param amount The amount to format
     * @param showDecimals Whether to show decimal places
     * @param showCurrencySymbol Whether to prefix with ₹
     * @return Formatted string like "₹1,23,456" or "₹1,23,456.00"
     */
    fun format(
        amount: Double,
        showDecimals: Boolean = false,
        showCurrencySymbol: Boolean = true
    ): String {
        val isNegative = amount < 0
        val absAmount = abs(amount)
        val prefix = if (showCurrencySymbol) "₹" else ""
        val negPrefix = if (isNegative) "-" else ""

        val formatted = formatIndian(absAmount, showDecimals)
        return "$negPrefix$prefix$formatted"
    }

    /**
     * Format with sign for display in transaction lists.
     * Income: +₹X,XXX, Expense: -₹X,XXX
     */
    fun formatWithSign(amount: Double, isIncome: Boolean): String {
        val sign = if (isIncome) "+" else "-"
        val absAmount = abs(amount)
        return "$sign₹${formatIndian(absAmount, false)}"
    }

    private fun formatIndian(amount: Double, showDecimals: Boolean): String {
        val longAmount = amount.toLong()
        val decimal = amount - longAmount

        val intPart = when {
            longAmount < 1_000 -> longAmount.toString()
            longAmount < 1_00_000 -> {
                val thousands = longAmount / 1_000
                val remainder = longAmount % 1_000
                "$thousands,${"%03d".format(remainder)}"
            }
            else -> formatLakhs(longAmount)
        }

        return if (showDecimals) {
            val decPart = "%02d".format((decimal * 100).toInt())
            "$intPart.$decPart"
        } else {
            intPart
        }
    }

    private fun formatLakhs(amount: Long): String {
        val result = StringBuilder()
        val str = amount.toString()
        val len = str.length

        // Last 3 digits
        result.insert(0, str.substring(len - 3))

        // Remaining digits in groups of 2
        var i = len - 3
        while (i > 0) {
            val start = maxOf(0, i - 2)
            result.insert(0, ",")
            result.insert(0, str.substring(start, i))
            i = start
        }

        return result.toString()
    }

    /**
     * Compact format for large numbers: ₹1.2L, ₹3.5Cr
     */
    fun formatCompact(amount: Double): String {
        val absAmount = abs(amount)
        val prefix = if (amount < 0) "-₹" else "₹"
        return when {
            absAmount >= 1_00_00_000 -> {
                val crores = absAmount / 1_00_00_000
                "$prefix${DecimalFormat("#.#").format(crores)}Cr"
            }
            absAmount >= 1_00_000 -> {
                val lakhs = absAmount / 1_00_000
                "$prefix${DecimalFormat("#.#").format(lakhs)}L"
            }
            absAmount >= 1_000 -> {
                val thousands = absAmount / 1_000
                "$prefix${DecimalFormat("#.#").format(thousands)}K"
            }
            else -> "$prefix${absAmount.toInt()}"
        }
    }
}
