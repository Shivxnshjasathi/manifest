package com.zincstate.manifest.feature.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zincstate.manifest.core.common.CurrencyFormatter
import com.zincstate.manifest.core.ui.components.TransactionItem
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@Composable
fun TransactionsWeekly(
    state: TransactionsUiState,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit,
    headerHeight: androidx.compose.ui.unit.Dp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item { Spacer(modifier = Modifier.height(headerHeight)) }

        if (state.weeklySummaries.isEmpty()) {
            item {
                Box(modifier = Modifier.fillParentMaxSize().padding(bottom = headerHeight), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No transactions this month",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ManifestThemeTokens.colors.textSecondary
                    )
                }
            }
            return@LazyColumn
        }

        items(state.weeklySummaries) { week ->
            WeeklyCard(
                week = week,
                isAmountVisible = state.isAmountVisible,
                selectedIds = state.selectedTransactionIds,
                onTransactionClick = onTransactionClick,
                onTransactionLongClick = onTransactionLongClick,
                onDeleteTransaction = onDeleteTransaction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeeklyCard(
    week: WeeklySummary,
    isAmountVisible: Boolean,
    selectedIds: Set<String>,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Week ${week.weekNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = week.dateRange,
                        style = MaterialTheme.typography.labelSmall,
                        color = ManifestThemeTokens.colors.textTertiary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isAmountVisible) CurrencyFormatter.format(week.net) else "••••",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (week.net >= 0) ManifestThemeTokens.colors.income else ManifestThemeTokens.colors.expense
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${week.transactions.size} txs",
                            style = MaterialTheme.typography.labelSmall,
                            color = ManifestThemeTokens.colors.textTertiary
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ManifestThemeTokens.colors.textTertiary
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    week.transactions.forEach { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    onDeleteTransaction(item.id)
                                }
                                false // Keep item until confirmed
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color = when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                                    else -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(color),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                    }
                                }
                            }
                        ) {
                            TransactionItem(
                                categoryIcon = item.categoryIcon,
                                categoryName = item.categoryName,
                                note = item.note,
                                accountName = item.accountName,
                                amount = item.amount,
                                isIncome = item.isIncome,
                                isAmountVisible = isAmountVisible,
                                isSelected = selectedIds.contains(item.id),
                                hasAttachment = item.hasAttachment,
                                onClick = { onTransactionClick(item.id) },
                                onLongClick = { onTransactionLongClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
