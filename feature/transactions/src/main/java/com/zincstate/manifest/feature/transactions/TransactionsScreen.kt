package com.zincstate.manifest.feature.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zincstate.manifest.core.common.DateUtils
import com.zincstate.manifest.core.ui.R
import com.zincstate.manifest.core.ui.components.*
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onTransactionClick: (String) -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterSheet by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<String?>(null) }
    var showBulkDeleteConfirmation by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(initialPage = state.selectedTab, pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    // Bulk Deletion Confirmation
    if (showBulkDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showBulkDeleteConfirmation = false },
            title = { Text("Delete Transactions") },
            text = { Text("Are you sure you want to delete ${state.selectedTransactionIds.size} transactions? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSelectedTransactions()
                        showBulkDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBulkDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Deletion Confirmation Dialog
    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        transactionToDelete?.let { viewModel.deleteTransactions(listOf(it)) }
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    val headerHeight = 220.dp

    // Sync ViewModel -> Pager
    LaunchedEffect(state.selectedTab) {
        if (pagerState.currentPage != state.selectedTab) {
            pagerState.animateScrollToPage(state.selectedTab)
        }
    }

    // Sync Pager -> ViewModel
    LaunchedEffect(pagerState.settledPage) {
        if (state.selectedTab != pagerState.settledPage) {
            viewModel.selectTab(pagerState.settledPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // CONTENT (Only this part swipes)
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isSearchActive) {
                SearchResults(
                    results = state.searchResults,
                    isAmountVisible = state.isAmountVisible,
                    selectedIds = state.selectedTransactionIds,
                    onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                    onTransactionLongClick = viewModel::toggleSelection,
                    onDeleteTransaction = { viewModel.deleteTransactions(listOf(it)) },
                    headerHeight = headerHeight
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
                            onDeleteTransaction = { transactionToDelete = it },
                            headerHeight = headerHeight
                        )
                        1 -> TransactionsCalendar(
                            state = state,
                            onDateSelect = viewModel::selectCalendarDate,
                            onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                            onTransactionLongClick = viewModel::toggleSelection,
                            onDeleteTransaction = { transactionToDelete = it },
                            headerHeight = headerHeight
                        )
                        2 -> TransactionsWeekly(
                            state = state,
                            onTransactionClick = { if (state.isSelectionMode) viewModel.toggleSelection(it) else onTransactionClick(it) },
                            onTransactionLongClick = viewModel::toggleSelection,
                            onDeleteTransaction = { transactionToDelete = it },
                            headerHeight = headerHeight
                        )
                        3 -> TransactionsMonthly(
                            state = state,
                            headerHeight = headerHeight
                        )
                    }
                }
            }
        }

        // FIXED HEADER (Stays consistent across tabs)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(MaterialTheme.colorScheme.background)
        ) {
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
                    onClearSelection = viewModel::clearSelection,
                    onDeleteSelected = { showBulkDeleteConfirmation = true }
                )
            }

            // Stats row
            AnimatedVisibility(visible = !state.isSearchActive) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.income),
                        amount = state.totalIncome,
                        isAmountVisible = state.isAmountVisible,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.expense),
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
                val tabs = listOf("Daily", "Calendar", "Weekly", "Monthly")
                ScrollableTabRow(
                    selectedTabIndex = pagerState.targetPage,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    edgePadding = 20.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        if (pagerState.targetPage < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.targetPage]),
                                color = MaterialTheme.colorScheme.primary,
                                height = 3.dp
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.targetPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (pagerState.targetPage == index) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (pagerState.targetPage == index) {
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
            val activeFilterCount = (if (state.filterType != null) 1 else 0) +
                state.filterAccountIds.size +
                state.filterCategoryIds.size +
                (if (state.filterMinAmount != null || state.filterMaxAmount != null) 1 else 0)

            BadgedBox(
                badge = {
                    if (activeFilterCount > 0) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text(activeFilterCount.toString())
                        }
                    }
                }
            ) {
                FloatingActionButton(
                    onClick = { showFilterSheet = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                state = state,
                onDismiss = { showFilterSheet = false },
                onApply = { type, accounts, categories, min, max, dates ->
                    viewModel.setFilter(type, accounts, categories, min, max, dates)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheet(
    state: TransactionsUiState,
    onDismiss: () -> Unit,
    onApply: (String?, Set<String>, Set<String>, Double?, Double?, Pair<LocalDate?, LocalDate?>?) -> Unit,
    onClear: () -> Unit
) {
    var selectedType by remember { mutableStateOf(state.filterType) }
    val selectedAccountIds = remember { mutableStateListOf<String>().apply { addAll(state.filterAccountIds) } }
    val selectedCategoryIds = remember { mutableStateListOf<String>().apply { addAll(state.filterCategoryIds) } }
    var minAmount by remember { mutableStateOf(state.filterMinAmount?.toString() ?: "") }
    var maxAmount by remember { mutableStateOf(state.filterMaxAmount?.toString() ?: "") }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.filter_transactions),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Transaction Type
            Text(
                stringResource(R.string.transaction_type),
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
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = if (selectedType == type) null else type },
                        label = { Text(type.lowercase().replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Amount Range
            Text(
                stringResource(R.string.amount_range),
                style = MaterialTheme.typography.labelLarge,
                color = ManifestThemeTokens.colors.textSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = minAmount,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) minAmount = it },
                    label = { Text(stringResource(R.string.min_hint)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = maxAmount,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) maxAmount = it },
                    label = { Text(stringResource(R.string.max_hint)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Accounts
            Text(
                stringResource(R.string.account),
                style = MaterialTheme.typography.labelLarge,
                color = ManifestThemeTokens.colors.textSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.accounts.forEach { account ->
                    val isSelected = selectedAccountIds.contains(account.id)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedAccountIds.remove(account.id)
                            else selectedAccountIds.add(account.id)
                        },
                        label = { Text(account.name) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Categories
            Text(
                stringResource(R.string.category),
                style = MaterialTheme.typography.labelLarge,
                color = ManifestThemeTokens.colors.textSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.categories.forEach { category ->
                    val isSelected = selectedCategoryIds.contains(category.id)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedCategoryIds.remove(category.id)
                            else selectedCategoryIds.add(category.id)
                        },
                        label = { Text("${category.icon} ${category.name}") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onClear,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(stringResource(R.string.clear))
                }
                
                Button(
                    onClick = {
                        onApply(
                            selectedType,
                            selectedAccountIds.toSet(),
                            selectedCategoryIds.toSet(),
                            minAmount.toDoubleOrNull(),
                            maxAmount.toDoubleOrNull(),
                            null
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(stringResource(R.string.apply))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyView(
    groupedTransactions: Map<String, List<TransactionUiItem>>,
    dailyTotals: Map<String, String>,
    isAmountVisible: Boolean,
    selectedIds: Set<String>,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit,
    headerHeight: androidx.compose.ui.unit.Dp
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 120.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.height(headerHeight)) }

        if (groupedTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillParentMaxSize().padding(bottom = headerHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📝",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.no_transactions),
                            style = MaterialTheme.typography.bodyLarge,
                            color = ManifestThemeTokens.colors.textSecondary
                        )
                        Text(
                            text = stringResource(R.string.add_first_transaction),
                            style = MaterialTheme.typography.bodySmall,
                            color = ManifestThemeTokens.colors.textTertiary
                        )
                    }
                }
            }
            return@LazyColumn
        }

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
                        hasAttachment = item.hasAttachment,
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
                    stringResource(R.string.search_hint),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResults(
    results: List<TransactionUiItem>,
    isAmountVisible: Boolean,
    selectedIds: Set<String>,
    onTransactionClick: (String) -> Unit,
    onTransactionLongClick: (String) -> Unit,
    onDeleteTransaction: (String) -> Unit,
    headerHeight: androidx.compose.ui.unit.Dp
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 120.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.height(headerHeight)) }

        if (results.isEmpty()) {
            item {
                Box(modifier = Modifier.fillParentMaxSize().padding(bottom = headerHeight), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ManifestThemeTokens.colors.textSecondary
                    )
                }
            }
            return@LazyColumn
        }

        item {
            Text(
                stringResource(R.string.results_count, results.size),
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
                    }
                    false // Keep item until confirmed
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
                    hasAttachment = item.hasAttachment,
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
    onClearSelection: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClearSelection) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Clear Selection",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Text(
                text = stringResource(R.string.selected_count, selectedCount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        
        IconButton(onClick = onDeleteSelected) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete Selected",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
