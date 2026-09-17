package com.zincstate.manifest.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@Composable
fun TransactionsMonthly(
    state: TransactionsUiState
) {
    if (state.monthlyCategoryTotals.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No transactions this month",
                style = MaterialTheme.typography.bodyLarge,
                color = ManifestThemeTokens.colors.textSecondary
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 120.dp, top = 8.dp)
    ) {
        val incomeTotals = state.monthlyCategoryTotals.filter { it.isIncome }
        val expenseTotals = state.monthlyCategoryTotals.filter { !it.isIncome }

        if (expenseTotals.isNotEmpty()) {
            item {
                Text(
                    text = "Expenses by Category",
                    style = MaterialTheme.typography.titleSmall,
                    color = ManifestThemeTokens.colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(expenseTotals, key = { it.categoryName }) { cat ->
                Box(modifier = Modifier.animateItem()) {
                    CategoryTotalRow(cat = cat, isAmountVisible = state.isAmountVisible)
                }
            }
        }

        if (incomeTotals.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Income by Category",
                    style = MaterialTheme.typography.titleSmall,
                    color = ManifestThemeTokens.colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(incomeTotals, key = { it.categoryName }) { cat ->
                Box(modifier = Modifier.animateItem()) {
                    CategoryTotalRow(cat = cat, isAmountVisible = state.isAmountVisible)
                }
            }
        }
    }
}

@Composable
private fun CategoryTotalRow(cat: CategoryTotalUiItem, isAmountVisible: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = cat.categoryIcon, fontSize = 20.sp)
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = cat.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // simple progress bar
                val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = cat.percentage,
                    animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000),
                    label = "progressAnimation"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(if (cat.isIncome) ManifestThemeTokens.colors.income else ManifestThemeTokens.colors.expense)
                    )
                }
                Text(
                    text = "${(cat.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = ManifestThemeTokens.colors.textTertiary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        
        Text(
            text = if (isAmountVisible) cat.totalAmount else "••••",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (cat.isIncome) ManifestThemeTokens.colors.income else MaterialTheme.colorScheme.onBackground
        )
    }
}
