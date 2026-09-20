package com.zincstate.manifest.feature.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MediumTopAppBar(
                title = {
                    Column {
                        Text(
                            "Manifest",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "Manifest your wealth",
                            style = MaterialTheme.typography.bodySmall,
                            color = ManifestThemeTokens.colors.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Backup Highlight
            item {
                Spacer(modifier = Modifier.height(16.dp))
                BackupCard(onBackupClick = {
                    val fileName = "manifest_backup_${java.time.LocalDate.now()}.db"
                    backupLauncher.launch(fileName)
                })
            }

            // Menu Groups
            item {
                SectionHeader("Planning")
                MenuContainer {
                    MenuItem(Icons.Default.BarChart, "Manage Budgets") { onNavigate("budgets") }
                    MenuDivider()
                    MenuItem(Icons.AutoMirrored.Filled.TrendingUp, "Savings Goals") { onNavigate("goals") }
                    MenuDivider()
                    MenuItem(Icons.Default.Repeat, "Subscriptions & Bills") { onNavigate("subscriptions") }
                }
            }

            item {
                SectionHeader("Accounts & Tools")
                MenuContainer {
                    MenuItem(Icons.Default.People, "Debts & Splits") { onNavigate("debts") }
                    MenuDivider()
                    MenuItem(Icons.Default.Sms, "Import from SMS") { onNavigate("sms-import") }
                    MenuDivider()
                    MenuItem(Icons.Default.Download, "Export to CSV") { viewModel.exportCsv() }
                    MenuDivider()
                    MenuItem(Icons.Default.Upload, "Import from CSV") { /* TODO */ }
                }
            }

            item {
                SectionHeader("Preferences")
                MenuContainer {
                    MenuItem(
                        icon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        label = if (isDarkTheme) "Light Mode" else "Dark Mode"
                    ) { viewModel.toggleTheme() }
                    MenuDivider()
                    MenuItem(
                        icon = Icons.Default.Palette,
                        label = "Dynamic Color"
                    ) { viewModel.toggleDynamicColor() }
                    MenuDivider()
                    MenuItem(Icons.Default.Security, "Privacy & Security") { onNavigate("privacy-security") }
                }
            }

            item {
                SectionHeader("About & Support")
                MenuContainer {
                    MenuItem(Icons.Default.BugReport, "Found a bug / Review") {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                            data = android.net.Uri.parse("https://forms.gle/VHc9uwFwZ5J1gf2A6")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle error
                        }
                    }
                    MenuDivider()
                    MenuItem(Icons.Default.Info, "Privacy Policy") { onNavigate("privacy-policy") }
                    MenuDivider()
                    MenuItem(Icons.Default.Email, "Contact Developer") {
                        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                            data = android.net.Uri.parse("mailto:contact.zincstate@gmail.com")
                            putExtra(android.content.Intent.EXTRA_SUBJECT, "Manifest App Feedback")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle case where no email app is installed
                        }
                    }
                }
            }

            // Danger Zone
            item {
                Spacer(modifier = Modifier.height(48.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = { viewModel.clearAllData() },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            Icons.Default.DeleteForever,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset Application Data")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "v1.0.0 • ZincState Manifest",
                        style = MaterialTheme.typography.labelSmall,
                        color = ManifestThemeTokens.colors.textTertiary
                    )
                }
            }
        }
    }
}

@Composable
private fun BackupCard(onBackupClick: () -> Unit) {
    Surface(
        onClick = onBackupClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Cloud Backup",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Keep your records safe on Google Drive",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        modifier = Modifier.padding(start = 32.dp, top = 24.dp, bottom = 8.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp
    )
}

@Composable
private fun MenuContainer(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
    ) {
        Column(content = content)
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
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
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
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}
