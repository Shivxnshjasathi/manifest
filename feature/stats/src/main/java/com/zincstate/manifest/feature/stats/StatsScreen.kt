package com.zincstate.manifest.feature.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zincstate.manifest.core.ui.components.PieChartComposable
import com.zincstate.manifest.core.ui.components.PieChartData
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun StatsScreen(viewModel: StatsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy") }

    val chartColors = listOf(
        Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFFC107), 
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF00BCD4)
    )

    val pieChartData = uiState.categoryBreakdown.mapIndexed { index, cat ->
        PieChartData(
            label = cat.categoryName,
            value = cat.totalAmount,
            color = chartColors[index % chartColors.size]
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Header with Month Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.previousMonth() }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }
            Text(
                text = uiState.currentMonth.format(monthFormatter),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp, start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            item {
                // Segmented Toggle for Income/Expense
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(4.dp)
                ) {
                    SegmentedButton(
                        text = "Income ${uiState.totalIncomeForMonth}",
                        isSelected = uiState.selectedType == "INCOME",
                        onClick = { viewModel.toggleType("INCOME") },
                        modifier = Modifier.weight(1f)
                    )
                    SegmentedButton(
                        text = "Expense ${uiState.totalExpenseForMonth}",
                        isSelected = uiState.selectedType == "EXPENSE",
                        onClick = { viewModel.toggleType("EXPENSE") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Donut Chart
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (pieChartData.isNotEmpty()) {
                        PieChartComposable(
                            data = pieChartData,
                            modifier = Modifier.size(240.dp),
                            strokeWidth = 100f // Donut chart width
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No transactions found for this period",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ManifestThemeTokens.colors.textSecondary
                            )
                        }
                    }
                }
            }

            // Categories List
            items(uiState.categoryBreakdown.size) { index ->
                val cat = uiState.categoryBreakdown[index]
                val color = chartColors[index % chartColors.size]
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ManifestThemeTokens.colors.elevated)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left colored vertical bar
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(24.dp)
                                .background(color, RoundedCornerShape(50))
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Percentage
                        Text(
                            text = "${(cat.percentage * 100).roundToInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(48.dp)
                        )
                        
                        // Icon + Name
                        Text(
                            text = "${cat.categoryIcon} ${cat.categoryName}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Amount
                        Text(
                            text = cat.formattedAmount,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.onBackground
                else Color.Transparent
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.background
            else ManifestThemeTokens.colors.textSecondary
        )
    }
}
