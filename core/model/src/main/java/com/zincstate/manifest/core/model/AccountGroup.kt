package com.zincstate.manifest.core.model

enum class AccountGroup(val displayName: String) {
    CASH("Cash"),
    BANK_ACCOUNTS("Bank Accounts"),
    CREDIT_CARD("Credit Card"),
    CARD("Card"),
    DEBIT_CARD("Debit Card"),
    SAVINGS("Savings"),
    TOP_UP_PREPAID("Top-Up & Prepaid"),
    INVESTMENTS("Investments"),
    OVERDRAFTS("Overdrafts"),
    LOAN("Loan"),
    INSURANCE("Insurance"),
    OTHERS("Others")
}
