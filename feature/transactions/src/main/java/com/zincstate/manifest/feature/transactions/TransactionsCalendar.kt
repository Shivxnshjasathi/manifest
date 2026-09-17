package com.zincstate.manifest.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zincstate.manifest.core.common.CurrencyFormatter
import com.zincstate.manifest.core.common.DateUtils
import com.zincstate.manifest.core.ui.components.DateHeader
import com.zincstate.manifest.core.ui.components.TransactionItem
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsCalendar(
    state: TransactionsUiState,
    onDateSelect: (String?) -> Unit,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        if (state.selectedCalendarDate == null) {
            onDateSelect(DateUtils.today())
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CalendarGrid(
            currentMonthStr = state.currentMonth,
            transactionDates = state.transactionDatesInMonth,
            dailyTotalsRaw = state.dailyTotalsRaw,
            selectedDate = state.selectedCalendarDate,
            onDateSelect = onDateSelect
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Filter transactions for the selected date, or show all if none selected
        val transactionsToShow = if (state.selectedCalendarDate != null) {
            state.groupedTransactions[state.selectedCalendarDate] ?: emptyList()
        } else {
            emptyList()
        }

        if (state.selectedCalendarDate != null) {
            if (transactionsToShow.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "No transactions on this date",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ManifestThemeTokens.colors.textSecondary
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    val formattedDate = try {
                        val date = LocalDate.parse(state.selectedCalendarDate)
                        val day = date.dayOfMonth
                        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()
                        "$day $dayOfWeek"
                    } catch (e: Exception) {
                        state.selectedCalendarDate
                    }
                    
                    item {
                        DateHeader(
                            dateText = formattedDate,
                            totalText = state.dailyTotals[state.selectedCalendarDate] ?: "",
                            isAmountVisible = state.isAmountVisible
                        )
                    }
                    
                    items(transactionsToShow, key = { it.id }) { item ->
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
                                        .padding(horizontal = 16.dp, vertical = 2.dp)
                                        .clip(RoundedCornerShape(12.dp))
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
                                note = item.note,
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
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Select a date to view transactions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ManifestThemeTokens.colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonthStr: String,
    transactionDates: Set<Int>,
    dailyTotalsRaw: Map<String, Double>,
    selectedDate: String?,
    onDateSelect: (String?) -> Unit
) {
    val yearMonth = try {
        YearMonth.parse(currentMonthStr)
    } catch (e: Exception) {
        YearMonth.now()
    }
    
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value // 1 (Mon) to 7 (Sun)
    
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Days of week header
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = ManifestThemeTokens.colors.textTertiary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar grid
        val totalCells = daysInMonth + firstDayOfWeek - 1
        val rows = Math.ceil(totalCells / 7.0).toInt()
        
        var dayCounter = 1
        
        for (i in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (j in 0 until 7) {
                    if (i == 0 && j < firstDayOfWeek - 1) {
                        // Empty cells before the 1st
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else if (dayCounter <= daysInMonth) {
                        val currentDay = dayCounter
                        val dateString = String.format("%04d-%02d-%02d", yearMonth.year, yearMonth.monthValue, currentDay)
                        val isSelected = dateString == selectedDate
                        val hasTransactions = transactionDates.contains(currentDay)
                        val dailyTotal = dailyTotalsRaw[dateString]
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else Color.Transparent
                                )
                                .clickable {
                                    if (isSelected) onDateSelect(null) else onDateSelect(dateString)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = currentDay.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onBackground
                                )
                                if (dailyTotal != null) {
                                    Text(
                                        text = CurrencyFormatter.formatCompact(dailyTotal),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                        else ManifestThemeTokens.colors.textTertiary,
                                        maxLines = 1
                                    )
                                } else if (hasTransactions && !isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                        dayCounter++
                    } else {
                        // Empty cells after the last day
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}
