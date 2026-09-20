package com.zincstate.manifest.core.model

enum class AccountGroup(val displayName: String, val isLiability: Boolean = false) {
    CASH("Cash"),
    BANK_ACCOUNTS("Bank Accounts"),
    CREDIT_CARD("Credit Card", isLiability = true),
    CARD("Card"),
    DEBIT_CARD("Debit Card"),
    SAVINGS("Savings"),
    TOP_UP_PREPAID("Top-Up & Prepaid"),
    INVESTMENTS("Investments"),
    OVERDRAFTS("Overdrafts", isLiability = true),
    LOAN("Loan", isLiability = true),
    INSURANCE("Insurance"),
    OTHERS("Others")
}
