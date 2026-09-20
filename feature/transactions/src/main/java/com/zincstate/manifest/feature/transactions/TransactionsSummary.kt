package com.zincstate.manifest.feature.transactions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zincstate.manifest.core.ui.components.PieChartComposable
import com.zincstate.manifest.core.ui.components.PieChartData
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@Composable
fun TransactionsSummary(
    state: TransactionsUiState,
    headerHeight: androidx.compose.ui.unit.Dp
) {
    val chartColors = listOf(
        Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFFC107), 
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF00BCD4)
    )

    val pieData = state.monthlyCategoryTotals
        .filter { !it.isIncome } // Only show expenses in pie chart
        .take(6)
        .mapIndexed { index, cat ->
            PieChartData(
                label = cat.categoryName,
                value = cat.rawAmount,
                color = chartColors[index % chartColors.size]
            )
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item { Spacer(modifier = Modifier.height(headerHeight)) }

        if (pieData.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PieChartComposable(
                        data = pieData,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .padding(vertical = 16.dp),
                        strokeWidth = 60f
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Top Expenses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        } else {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No expense data for chart",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ManifestThemeTokens.colors.textSecondary
                    )
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Summary Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Income", color = ManifestThemeTokens.colors.textSecondary)
                Text(
                    text = if (state.isAmountVisible) state.totalIncome else "••••",
                    color = ManifestThemeTokens.colors.income,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Expense", color = ManifestThemeTokens.colors.textSecondary)
                Text(
                    text = if (state.isAmountVisible) state.totalExpense else "••••",
                    color = ManifestThemeTokens.colors.expense,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Net Savings", color = ManifestThemeTokens.colors.textSecondary)
                Text(
                    text = if (state.isAmountVisible) state.total else "••••",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
