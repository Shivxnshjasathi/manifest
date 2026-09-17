package com.zincstate.manifest.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zincstate.manifest.core.ui.components.TransactionItem
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsDescription(
    state: TransactionsUiState,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    if (state.descriptionGroupedTransactions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No transactions this month",
                style = MaterialTheme.typography.bodyLarge,
                color = ManifestThemeTokens.colors.textSecondary
            )
        }
        return
    }

    // Sort by count (highest first) then by description name
    val sortedDescriptions = state.descriptionGroupedTransactions.entries.sortedWith(
        compareByDescending<Map.Entry<String, List<TransactionUiItem>>> { it.value.size }
            .thenBy { it.key }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        sortedDescriptions.forEach { (desc, txs) ->
            item(key = "header_$desc") {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${txs.size} transaction${if (txs.size > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ManifestThemeTokens.colors.textTertiary
                    )
                }
            }

            items(txs, key = { it.id }) { item ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            onDeleteTransaction(item.id)
                            true
                        } else false
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
                    },
                    modifier = Modifier.animateItem()
                ) {
                    TransactionItem(
                        categoryIcon = item.categoryIcon,
                        categoryName = item.categoryName,
                        note = item.date, // Show date instead of note since note is the header
                        accountName = item.accountName,
                        amount = item.amount,
                        isIncome = item.isIncome,
                        isAmountVisible = state.isAmountVisible,
                        isSelected = state.selectedTransactionIds.contains(item.id),
                        onClick = { onTransactionClick(item.id) },
                        onLongClick = { onTransactionLongClick(item.id) }
                    )
                }
            }
        }
    }
}
