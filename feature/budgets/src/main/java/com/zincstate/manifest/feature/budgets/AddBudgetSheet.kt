package com.zincstate.manifest.feature.budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetSheet(
    onDismiss: () -> Unit,
    onSave: (categoryId: String, amount: Double) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var categoryId by remember { mutableStateOf("overall") }
    var amount by remember { mutableStateOf("") }
    
    // For Phase 3, keeping categories simple. A real implementation would pull from CategoryDao
    val categories = listOf("overall", "Housing", "Food", "Transportation", "Entertainment")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp, top = 8.dp)
        ) {
            Text(
                "Set Budget",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text("Category", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 8.dp))
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = categoryId == cat,
                        onClick = { categoryId = cat },
                        label = { Text(cat.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monthly Limit") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val limit = amount.toDoubleOrNull() ?: 0.0
                    if (limit > 0) {
                        scope.launch {
                            sheetState.hide()
                            onSave(categoryId, limit)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = amount.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text("Save Budget", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
