package com.zincstate.manifest.feature.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@Composable
fun MoreScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: MoreViewModel = hiltViewModel()
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle(initialValue = true)
    val dynamicColor by viewModel.dynamicColor.collectAsStateWithLifecycle(initialValue = true)
    val context = androidx.compose.ui.platform.LocalContext.current

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        uri?.let { viewModel.backupDatabase(context, it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // App info card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            "Manifest",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Manifest your wealth",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ManifestThemeTokens.colors.textSecondary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        val fileName = "manifest_backup_${java.time.LocalDate.now()}.db"
                        backupLauncher.launch(fileName)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CloudSync,
                        contentDescription = "Cloud Sync",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Backup to Google Drive",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Export your data to a secure file. Select your Google Drive folder to keep it backed up.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ManifestThemeTokens.colors.textSecondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Menu items
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, ManifestThemeTokens.colors.border),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {
                    MenuItem(Icons.Default.BarChart, "Manage Budgets") { onNavigate("budgets") }
                    MenuDivider()
                    MenuItem(Icons.AutoMirrored.Filled.TrendingUp, "Savings Goals") { onNavigate("goals") }
                    MenuDivider()
                    MenuItem(Icons.Default.Repeat, "Subscriptions & Bills") { onNavigate("subscriptions") }
                    MenuDivider()
                    MenuItem(Icons.Default.People, "Debts & Splits") { onNavigate("debts") }
                    MenuDivider()
                    MenuItem(Icons.Default.Sms, "Import from SMS") { onNavigate("sms-import") }
                    MenuDivider()
                    MenuItem(Icons.Default.Download, "Export to CSV") { viewModel.exportCsv() }
                    MenuDivider()
                    MenuItem(Icons.Default.Upload, "Import from CSV") { /* TODO */ }
                    MenuDivider()
                    MenuItem(
                        icon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        label = if (isDarkTheme) "Light Mode" else "Dark Mode"
                    ) { viewModel.toggleTheme() }
                    MenuDivider()
                    MenuItem(
                        icon = Icons.Default.Palette,
                        label = "Dynamic Color (Material You)"
                    ) { viewModel.toggleDynamicColor() }
                    MenuDivider()
                    MenuItem(Icons.Default.Security, "Privacy & Security") { onNavigate("privacy-security") }
                }
            }
        }

        // Danger zone
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { viewModel.clearAllData() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x1AFF5A5F)
                ),
                border = BorderStroke(1.dp, Color(0x33FF5A5F)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = Color(0xFFFF5A5F),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Clear All Data",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF5A5F)
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ManifestThemeTokens.colors.textTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        color = ManifestThemeTokens.colors.border,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
