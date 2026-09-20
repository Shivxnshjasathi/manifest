package com.zincstate.manifest.feature.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@Composable
fun AccountsScreen(
    onAccountClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    var showAddSheet by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (showAddSheet) {
        AddAccountSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name, group, balance ->
                viewModel.addAccount(name, group, balance)
                showAddSheet = false
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
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                "Accounts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showAddSheet = true }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Account",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Account list grouped by type
        val assets = accounts.filter { account ->
            val group = try { com.zincstate.manifest.core.model.AccountGroup.valueOf(account.group) } catch(_: Exception) { null }
            group?.isLiability == false || group == null
        }
        val liabilities = accounts.filter { account ->
            val group = try { com.zincstate.manifest.core.model.AccountGroup.valueOf(account.group) } catch(_: Exception) { null }
            group?.isLiability == true
        }

        if (accounts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No accounts yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ManifestThemeTokens.colors.textSecondary
                )
            }
        } else {
            val totalAssets = assets.sumOf { it.balance }
            val totalLiabilities = liabilities.sumOf { it.balance }
            val netWorth = totalAssets - totalLiabilities
            
            val netWorthFormatted = com.zincstate.manifest.core.common.CurrencyFormatter.format(netWorth)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Net Worth",
                            style = MaterialTheme.typography.labelLarge,
                            color = ManifestThemeTokens.colors.textSecondary
                        )
                        Text(
                            text = netWorthFormatted,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "Assets: ${com.zincstate.manifest.core.common.CurrencyFormatter.format(totalAssets)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = ManifestThemeTokens.colors.income
                            )
                            Text(
                                text = "Liabilities: ${com.zincstate.manifest.core.common.CurrencyFormatter.format(totalLiabilities)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = ManifestThemeTokens.colors.expense
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (assets.isNotEmpty()) {
                    item {
                        Text(
                            text = "ASSETS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ManifestThemeTokens.colors.textTertiary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(items = assets, key = { it.id }) { account ->
                        Box(modifier = Modifier.animateItem()) {
                            AccountItem(account, onAccountClick)
                        }
                    }
                }

                if (liabilities.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "LIABILITIES",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ManifestThemeTokens.colors.textTertiary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(items = liabilities, key = { it.id }) { account ->
                        Box(modifier = Modifier.animateItem()) {
                            AccountItem(account, onAccountClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountItem(account: AccountUiItem, onAccountClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { onAccountClick(account.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                account.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    account.balanceFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = ManifestThemeTokens.colors.textTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
