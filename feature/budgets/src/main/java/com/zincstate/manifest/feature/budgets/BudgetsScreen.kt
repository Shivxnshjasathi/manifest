package com.zincstate.manifest.feature.budgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zincstate.manifest.core.common.CurrencyFormatter
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens
import java.time.format.DateTimeFormatter

@Composable
fun BudgetsScreen(
    onBackClick: () -> Unit,
    viewModel: BudgetsViewModel = hiltViewModel()
) {
    val currentMonth by viewModel.currentMonth.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    
    var showAddSheet by remember { mutableStateOf(false) }

    if (showAddSheet) {
        AddBudgetSheet(
            onDismiss = { showAddSheet = false },
            onSave = { categoryId, amount ->
                viewModel.addBudget(categoryId, amount)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                "Budgets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { showAddSheet = true }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Budget",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Month Navigation
        val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.previousMonth() }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }
            Text(
                text = currentMonth.format(monthFormatter),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
        }

        if (budgets.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No budgets set for this month", color = ManifestThemeTokens.colors.textSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showAddSheet = true }) {
                        Text("Create Budget")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp, start = 16.dp, end = 16.dp)
            ) {
                items(budgets, key = { it.id }) { budget ->
                    Box(modifier = Modifier.animateItem()) {
                        BudgetCard(
                            budget = budget,
                            onDelete = { viewModel.deleteBudget(budget.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    budget: BudgetUiItem,
    onDelete: () -> Unit
) {
    val progress = if (budget.limit > 0) (budget.spent / budget.limit).coerceIn(0.0, 1.0).toFloat() else 0f
    val isOverBudget = budget.spent > budget.limit

    val animatedProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = progress,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000),
        label = "budgetProgress"
    )

    val progressColor = when {
        isOverBudget -> MaterialTheme.colorScheme.error
        progress > 0.8f -> Color(0xFFFFC107) // Warning yellow
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = budget.categoryId.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = CurrencyFormatter.format(budget.spent) + " spent",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "of " + CurrencyFormatter.format(budget.limit),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ManifestThemeTokens.colors.textSecondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round,
            )
            if (isOverBudget) {
                Text(
                    text = "Over budget by ${CurrencyFormatter.format(budget.spent - budget.limit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
