package com.zincstate.manifest.core.ui.theme

import androidx.compose.ui.graphics.Color

// Dark Mode Colors (Default)
val DarkBackground = Color(0xFF000000)       // Pure black OLED
val DarkSurface = Color(0xFF121212)
val DarkElevated = Color(0xFF1C1C1E)
val DarkBorder = Color(0x1FFFFFFF)           // rgba(255,255,255,0.12)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFA1A1AA)
val DarkTextTertiary = Color(0xFF52525B)
val DarkAccent = Color(0xFFFFFFFF)           // Pure white accent
val DarkIncome = Color(0xFFFFFFFF)
val DarkExpense = Color(0xFFA1A1AA)
val DarkTransfer = Color(0xFF71717A)

// Light Mode Colors
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFF2F2F7)
val LightElevated = Color(0xFFE5E5EA)
val LightBorder = Color(0x1F000000)          // rgba(0,0,0,0.12)
val LightTextPrimary = Color(0xFF000000)
val LightTextSecondary = Color(0xFF52525B)
val LightTextTertiary = Color(0xFFA1A1AA)
val LightAccent = Color(0xFF000000)          // Pure black accent
val LightIncome = Color(0xFF000000)
val LightExpense = Color(0xFF52525B)
val LightTransfer = Color(0xFF71717A)

// Semantic Colors (shared)
val BudgetGreen = Color(0xFF10AC84)
val BudgetOrange = Color(0xFFFF9F43)
val BudgetRed = Color(0xFFFF5A5F)

// Goal preset colors
val GoalColors = listOf(
    Color(0xFFFF5A5F),
    Color(0xFFFF9F43),
    Color(0xFFFECA57),
    Color(0xFF48DBFB),
    Color(0xFF0ABDE3),
    Color(0xFF10AC84),
    Color(0xFFEE5A24),
    Color(0xFFA29BFE),
    Color(0xFFFD79A8)
)
