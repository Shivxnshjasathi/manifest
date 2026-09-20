package com.zincstate.manifest.core.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

/**
 * Formats amounts using the Indian numbering system or standard system based on preference.
 */
object CurrencyFormatter {

    /**
     * Format amount with optional custom currency symbol.
     * @param amount The amount to format
     * @param showDecimals Whether to show decimal places
     * @param currencySymbol Custom currency symbol (defaults to ₹)
     * @return Formatted string
     */
    fun format(
        amount: Double,
        showDecimals: Boolean = false,
        currencySymbol: String = "₹"
    ): String {
        val isNegative = amount < 0
        val absAmount = abs(amount)
        val negPrefix = if (isNegative) "-" else ""

        val formatted = if (currencySymbol == "₹") formatIndian(absAmount, showDecimals) else formatStandard(absAmount, showDecimals)
        return "$negPrefix$currencySymbol$formatted"
    }

    /**
     * Format with sign for display in transaction lists.
     */
    fun formatWithSign(amount: Double, isIncome: Boolean, currencySymbol: String = "₹"): String {
        val sign = if (isIncome) "+" else "-"
        val absAmount = abs(amount)
        val formatted = if (currencySymbol == "₹") formatIndian(absAmount, false) else formatStandard(absAmount, false)
        return "$sign$currencySymbol$formatted"
    }

    private fun formatStandard(amount: Double, showDecimals: Boolean): String {
        val pattern = if (showDecimals) "#,##0.00" else "#,##0"
        return DecimalFormat(pattern, DecimalFormatSymbols(Locale.US)).format(amount)
    }

    private fun formatIndian(amount: Double, showDecimals: Boolean): String {
        val longAmount = amount.toLong()
        val decimal = amount - longAmount

        val intPart = when {
            longAmount < 1_000 -> longAmount.toString()
            longAmount < 1_00_00_000 -> formatLakhs(longAmount) // Use Indian grouping for anything above 1000 in INR
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
        val str = amount.toString()
        val len = str.length
        if (len <= 3) return str

        val lastThree = str.substring(len - 3)
        val remaining = str.substring(0, len - 3)
        
        val result = StringBuilder()
        var i = remaining.length
        while (i > 0) {
            val start = maxOf(0, i - 2)
            if (result.isNotEmpty()) result.insert(0, ",")
            result.insert(0, remaining.substring(start, i))
            i = start
        }
        
        return "$result,$lastThree"
    }

    /**
     * Compact format for large numbers.
     */
    fun formatCompact(amount: Double, currencySymbol: String = "₹"): String {
        val absAmount = abs(amount)
        val prefix = if (amount < 0) "-$currencySymbol" else currencySymbol
        
        if (currencySymbol == "₹") {
            return when {
                absAmount >= 1_00_00_000 -> "$prefix${DecimalFormat("#.#").format(absAmount / 1_00_00_000)}Cr"
                absAmount >= 1_00_000 -> "$prefix${DecimalFormat("#.#").format(absAmount / 1_00_000)}L"
                absAmount >= 1_000 -> "$prefix${DecimalFormat("#.#").format(absAmount / 1_000)}K"
                else -> "$prefix${absAmount.toInt()}"
            }
        } else {
            return when {
                absAmount >= 1_000_000_000 -> "$prefix${DecimalFormat("#.#").format(absAmount / 1_000_000_000)}B"
                absAmount >= 1_000_000 -> "$prefix${DecimalFormat("#.#").format(absAmount / 1_000_000)}M"
                absAmount >= 1_000 -> "$prefix${DecimalFormat("#.#").format(absAmount / 1_000)}K"
                else -> "$prefix${absAmount.toInt()}"
            }
        }
    }
}
