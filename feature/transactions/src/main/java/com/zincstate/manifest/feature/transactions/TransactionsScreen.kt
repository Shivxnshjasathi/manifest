package com.zincstate.manifest.feature.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.draw.clip
import com.zincstate.manifest.core.common.DateUtils
import com.zincstate.manifest.core.ui.components.DateHeader
import com.zincstate.manifest.core.ui.components.StatCard
import com.zincstate.manifest.core.ui.components.TransactionItem
import com.zincstate.manifest.core.ui.components.TransactionTopBar
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onTransactionClick: (String) -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (abs(dragAmount) > 50) {
                        if (dragAmount > 0) {
                            viewModel.navigateMonth(-1)
                        } else {
                            viewModel.navigateMonth(1)
                        }
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
        // ... (rest of the Column content)
        // Search overlay or Top bar
        AnimatedVisibility(
            visible = state.isSearchActive,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = viewModel::updateSearchQuery,
                onClose = viewModel::toggleSearch
            )
        }

        AnimatedVisibility(
            visible = !state.isSearchActive && !state.isSelectionMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TransactionTopBar(
                monthYearText = state.monthDisplayText,
                isAmountVisible = state.isAmountVisible,
                onSearchClick = viewModel::toggleSearch,
                onToggleVisibility = viewModel::toggleAmountVisibility,
                onPreviousMonth = { viewModel.navigateMonth(-1) },
                onNextMonth = { viewModel.navigateMonth(1) }
            )
        }

        AnimatedVisibility(
            visible = state.isSelectionMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SelectionTopBar(
                selectedCount = state.selectedTransactionIds.size,
                onClearSelection = viewModel::clearSelection
            )
        }

        // Stats row
        AnimatedVisibility(visible = !state.isSearchActive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    label = "Income",
                    amount = state.totalIncome,
                    isAmountVisible = state.isAmountVisible,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Expense",
                    amount = state.totalExpense,
                    isAmountVisible = state.isAmountVisible,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Total",
                    amount = state.total,
                    isAmountVisible = state.isAmountVisible,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Tabs
        if (!state.isSearchActive) {
            val tabs = listOf("Daily", "Calendar", "Monthly", "Summary", "Description")
            ScrollableTabRow(
                selectedTabIndex = state.selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                edgePadding = 16.dp,
                divider = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = state.selectedTab == index,
                        onClick = { viewModel.selectTab(index) },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (state.selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (state.selectedTab == index) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    ManifestThemeTokens.colors.textTertiary
                                }
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Content
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
        
        LaunchedEffect(state.selectedTab) {
            if (pagerState.currentPage != state.selectedTab) {
                pagerState.animateScrollToPage(state.selectedTab)
            }
        }
        
        LaunchedEffect(pagerState.currentPage) {
            if (state.selectedTab != pagerState.currentPage) {
                viewModel.selectTab(pagerState.currentPage)
            }
        }

        if (state.isSearchActive) {
            SearchResults(
                results = state.searchResults,
                isAmountVisible = state.isAmountVisible,
                selectedIds = state.selectedTransactionIds,
                onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                onTransactionLongClick = viewModel::toggleSelection,
                onDeleteTransaction = { viewModel.deleteTransactions(listOf(it)) }
            )
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = !state.isSelectionMode
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> DailyView(
                        groupedTransactions = state.groupedTransactions,
                        dailyTotals = state.dailyTotals,
                        isAmountVisible = state.isAmountVisible,
                        selectedIds = state.selectedTransactionIds,
                        onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                        onTransactionLongClick = viewModel::toggleSelection,
                        onDeleteTransaction = { viewModel.deleteTransactions(listOf(it)) }
                    )
                    1 -> TransactionsCalendar(
                        state = state,
                        onDateSelect = viewModel::selectCalendarDate,
                        onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                        onTransactionLongClick = viewModel::toggleSelection,
                        onDeleteTransaction = { viewModel.deleteTransactions(listOf(it)) }
                    )
                    2 -> TransactionsMonthly(state = state)
                    3 -> TransactionsSummary(state = state)
                    4 -> TransactionsDescription(
                        state = state,
                        onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                        onTransactionLongClick = viewModel::toggleSelection,
                        onDeleteTransaction = { viewModel.deleteTransactions(listOf(it)) }
                    )
                }
            }
        }
        }

        // Floating Filter Button
        AnimatedVisibility(
            visible = !state.isSearchActive && !state.isSelectionMode,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 120.dp, end = 16.dp)
        ) {
            androidx.compose.material3.FloatingActionButton(
                onClick = { showFilterSheet = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.FilterList,
                    contentDescription = "Filter"
                )
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                selectedType = state.filterType,
                onDismiss = { showFilterSheet = false },
                onApply = { type ->
                    viewModel.setFilter(type, null, null)
                    showFilterSheet = false
                },
                onClear = {
                    viewModel.clearFilters()
                    showFilterSheet = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    selectedType: String?,
    onDismiss: () -> Unit,
    onApply: (String?) -> Unit,
    onClear: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { androidx.compose.material3.BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Filter Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Transaction Type",
                style = MaterialTheme.typography.labelLarge,
                color = ManifestThemeTokens.colors.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("INCOME", "EXPENSE", "TRANSFER").forEach { type ->
                    androidx.compose.material3.FilterChip(
                        selected = selectedType == type,
                        onClick = { onApply(if (selectedType == type) null else type) },
                        label = { Text(type.lowercase().replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            androidx.compose.material3.Button(
                onClick = onClear,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Clear Filters")
            }
        }
    }
}

@Composable
private fun DailyView(
    groupedTransactions: Map<String, List<TransactionUiItem>>,
    dailyTotals: Map<String, String>,
    isAmountVisible: Boolean,
    selectedIds: Set<String>,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    if (groupedTransactions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "📝",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No transactions yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ManifestThemeTokens.colors.textSecondary
                )
                Text(
                    text = "Tap + to add your first transaction",
                    style = MaterialTheme.typography.bodySmall,
                    color = ManifestThemeTokens.colors.textTertiary
                )
            }
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 120.dp), // space for bottom nav
        modifier = Modifier.fillMaxSize()
    ) {
        groupedTransactions.forEach { (isoDate, transactions) ->
            item(key = "header_$isoDate") {
                DateHeader(
                    dateText = DateUtils.formatDate(isoDate),
                    totalText = dailyTotals[isoDate] ?: "",
                    isAmountVisible = isAmountVisible
                )
            }

            items(
                items = transactions,
                key = { it.id }
            ) { item ->
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
                        note = item.note,
                        accountName = item.accountName,
                        amount = item.amount,
                        isIncome = item.isIncome,
                        isAmountVisible = isAmountVisible,
                        isSelected = selectedIds.contains(item.id),
                        onClick = { onTransactionClick(item.id) },
                        onLongClick = { onTransactionLongClick(item.id) }
                    )
                }
            }

            item(key = "divider_$isoDate") {
                HorizontalDivider(
                    color = ManifestThemeTokens.colors.border,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    "Search transactions...",
                    color = ManifestThemeTokens.colors.textTertiary
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = ManifestThemeTokens.colors.textSecondary
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )
        IconButton(onClick = onClose) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close search",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun SearchResults(
    results: List<TransactionUiItem>,
    isAmountVisible: Boolean,
    selectedIds: Set<String>,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    if (results.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "No results found",
                style = MaterialTheme.typography.bodyMedium,
                color = ManifestThemeTokens.colors.textSecondary
            )
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 120.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                "${results.size} results",
                style = MaterialTheme.typography.labelMedium,
                color = ManifestThemeTokens.colors.textTertiary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        items(items = results, key = { it.id }) { item ->
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
                    val color = when (dismissState.dismissDirection) {
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
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
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
                    isAmountVisible = isAmountVisible,
                    isSelected = selectedIds.contains(item.id),
                    onClick = { onTransactionClick(item.id) },
                    onLongClick = { onTransactionLongClick(item.id) }
                )
            }
        }
    }
}

@Composable
fun SelectionTopBar(
    selectedCount: Int,
    onClearSelection: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClearSelection) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Clear Selection",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text = "$selectedCount Selected",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

// Removed Placeholders
