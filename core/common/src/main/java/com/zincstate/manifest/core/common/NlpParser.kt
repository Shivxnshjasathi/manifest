package com.zincstate.manifest.core.common

import com.zincstate.manifest.core.model.TransactionType

/**
 * Parses natural language input to extract transaction details.
 * 
 * Examples:
 * - "spent 500 on food" → type=EXPENSE, amount=500, note="Food"
 * - "got 25000 salary" → type=INCOME, amount=25000, note="Salary"
 * - "paid 200 for cab" → type=EXPENSE, amount=200, note="Cab"
 * - "received 1000 refund" → type=INCOME, amount=1000, note="Refund"
 */
object NlpParser {

    private val CATEGORY_KEYWORDS = mapOf(
        "food" to "Food",
        "restaurant" to "Food",
        "dinner" to "Food",
        "lunch" to "Food",
        "breakfast" to "Food",
        "zomato" to "Food",
        "swiggy" to "Food",
        "groceries" to "Groceries",
        "grocery" to "Groceries",
        "milk" to "Groceries",
        "market" to "Groceries",
        "shopping" to "Apparel",
        "clothes" to "Apparel",
        "dress" to "Apparel",
        "beauty" to "Beauty",
        "parlor" to "Beauty",
        "salon" to "Beauty",
        "makeup" to "Beauty",
        "health" to "Health",
        "medicine" to "Health",
        "doctor" to "Health",
        "hospital" to "Health",
        "pharmacy" to "Health",
        "bank" to "Other",
        "interest" to "Income",
        "salary" to "Income",
        "income" to "Income",
        "transport" to "Transport",
        "uber" to "Transport",
        "ola" to "Transport",
        "cab" to "Transport",
        "auto" to "Transport",
        "petrol" to "Transport",
        "fuel" to "Transport",
        "bus" to "Transport",
        "train" to "Transport",
        "metro" to "Transport",
        "household" to "Household",
        "rent" to "Household",
        "electricity" to "Household",
        "water" to "Household",
        "gas" to "Household",
        "cleaning" to "Household",
        "education" to "Education",
        "fees" to "Education",
        "book" to "Education",
        "school" to "Education",
        "college" to "Education",
        "gift" to "Gift",
        "present" to "Gift",
        "charity" to "Gift",
        "culture" to "Culture",
        "movie" to "Culture",
        "theatre" to "Culture",
        "concert" to "Culture",
        "travel" to "Culture",
        "hotel" to "Culture",
        "social" to "Social Life",
        "party" to "Social Life",
        "club" to "Social Life",
        "bar" to "Social Life",
        "drink" to "Social Life",
        "pets" to "Pets",
        "dog" to "Pets",
        "cat" to "Pets",
        "vet" to "Pets"
    )

    data class ParsedTransaction(
        val amount: Double? = null,
        val type: TransactionType = TransactionType.EXPENSE,
        val note: String = "",
        val categoryName: String? = null,
        val accountName: String? = null,
        val rawInput: String = ""
    )

    private val EXPENSE_KEYWORDS = setOf(
        "spent", "paid", "bought", "gave", "cost", "purchased",
        "expense", "spend", "pay", "buy", "purchase"
    )

    private val INCOME_KEYWORDS = setOf(
        "got", "received", "earned", "salary", "income", "refund",
        "won", "earn", "receive", "get"
    )

    private val STOP_WORDS = setOf(
        "on", "for", "at", "to", "from", "in", "a", "the", "an",
        "today", "yesterday", "tomorrow", "rupees", "bucks", "rs",
        "inr", "usd", "dollars", "my", "i", "and", "with"
    )

    // Matches amounts like 500, ₹500, Rs500, Rs.500, $500, 1,000, 12,345.67
    private val AMOUNT_REGEX = Regex("""(?:₹|Rs\.?|rs\.?|\$)?\s*(\d[\d,]*\.?\d*)""")

    fun parse(input: String): ParsedTransaction {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ParsedTransaction(rawInput = trimmed)

        val words = trimmed.lowercase().split(Regex("\\s+"))

        // Extract amount
        val amountMatch = AMOUNT_REGEX.find(trimmed)
        val amount = amountMatch?.groupValues?.get(1)
            ?.replace(",", "")
            ?.toDoubleOrNull()

        // Detect type from keywords
        val type = when {
            words.any { it in INCOME_KEYWORDS } -> TransactionType.INCOME
            words.any { it in EXPENSE_KEYWORDS } -> TransactionType.EXPENSE
            else -> TransactionType.EXPENSE // Default
        }

        // Detect account: look for words after "from" or "via"
        var accountName: String? = null
        val accountTriggers = listOf("from", "via", "by", "using")
        var accountTriggerIndex = -1
        for (i in 0 until words.size - 1) {
            if (words[i] in accountTriggers) {
                accountName = words[i + 1]
                accountTriggerIndex = i
                break
            }
        }

        // Extract note: remove amounts, type keywords, stop words, and account info
        val noteWords = words
            .filterIndexed { index, word ->
                word !in EXPENSE_KEYWORDS &&
                word !in INCOME_KEYWORDS &&
                word !in STOP_WORDS &&
                !word.matches(Regex("""[\d₹$,.]+""")) &&
                !word.matches(Regex("""rs\.?\d*""")) &&
                index != accountTriggerIndex &&
                index != (accountTriggerIndex + 1)
            }
            .map { it.replaceFirstChar { c -> c.uppercase() } }

        val note = noteWords.joinToString(" ").trim()

        // Detect category from keywords
        val categoryName = words.mapNotNull { CATEGORY_KEYWORDS[it] }.firstOrNull()

        return ParsedTransaction(
            amount = amount,
            type = type,
            note = note,
            categoryName = categoryName,
            accountName = accountName,
            rawInput = trimmed
        )
    }
}
