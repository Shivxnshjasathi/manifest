package com.zincstate.manifest.feature.sms

import java.util.regex.Pattern

data class ParsedSms(
    val amount: Double,
    val isExpense: Boolean,
    val accountName: String,
    val originalText: String,
    val dateStr: String? = null // For when date extraction from SMS is supported
)

object SmsParser {

    // Regex for typical Indian bank SMS format
    // Example: "Sent Rs.500.00 from HDFC Bank A/c xx1234 to ..."
    // Example: "Rs.500.00 debited from A/c xx1234 on 05-09-24 to ..."
    // Example: "Your A/c xx1234 is credited with INR 500.00 on 05-09-24..."

    // Relaxed heuristics for currency symbols and keywords
    private val AMOUNT_PATTERN = Pattern.compile("(?i)(?:Rs\\.?|INR|[$₹€])\\s*([0-9,]+\\.?[0-9]*)")
    private val ACCOUNT_PATTERN = Pattern.compile("(?i)(?:A/c|acct|account|a/c no\\.?)[\\s:-]*X*([0-9]{3,})")

    // Keywords to determine type
    private val DEBIT_KEYWORDS = listOf("debited", "spent", "sent", "deducted", "paid")
    private val CREDIT_KEYWORDS = listOf("credited", "received", "added")

    fun parse(smsText: String): ParsedSms? {
        val amountMatcher = AMOUNT_PATTERN.matcher(smsText)
        if (!amountMatcher.find()) return null
        
        val amountStr = amountMatcher.group(1)?.replace(",", "") ?: return null
        val amount = amountStr.toDoubleOrNull() ?: return null

        val accountMatcher = ACCOUNT_PATTERN.matcher(smsText)
        val accountSuffix = if (accountMatcher.find()) accountMatcher.group(1) else "Unknown"

        val lowerText = smsText.lowercase()
        
        val isExpense = DEBIT_KEYWORDS.any { lowerText.contains(it) }
        val isIncome = CREDIT_KEYWORDS.any { lowerText.contains(it) }

        if (!isExpense && !isIncome) return null // Unsure if it's a transaction

        // For simplicity, we just return true for expense unless it's explicitly income.
        val resolvedIsExpense = if (isExpense) true else !isIncome

        // Determine bank from sender or text
        val bankName = when {
            lowerText.contains("hdfc") -> "HDFC"
            lowerText.contains("sbi") || lowerText.contains("state bank") -> "SBI"
            lowerText.contains("icici") -> "ICICI"
            lowerText.contains("axis") -> "Axis"
            else -> "Bank"
        }

        return ParsedSms(
            amount = amount,
            isExpense = resolvedIsExpense,
            accountName = "$bankName xx$accountSuffix",
            originalText = smsText
        )
    }
}
