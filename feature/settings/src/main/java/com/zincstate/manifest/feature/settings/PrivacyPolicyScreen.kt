package com.zincstate.manifest.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    "Privacy Policy",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Last Updated: October 2023",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PolicySection(
                title = "1. Introduction",
                content = "Welcome to Manifest, a personal finance management application developed by Zincstate (\"we\", \"us\", or \"our\"). We are committed to protecting your personal information and your right to privacy. This Privacy Policy applies to all information collected through our mobile application (Manifest) and any related services."
            )

            PolicySection(
                title = "2. Information We Collect",
                content = "Manifest is designed to be a local-first application. We prioritize your privacy by keeping your data on your device.\n\n" +
                        "• Financial Data: We collect transaction history, account balances, and budget details that you manually enter or import via SMS. This data is stored locally on your device.\n" +
                        "• SMS Data: If you grant SMS permissions, the app parses financial SMS messages locally to automate expense tracking. We do NOT upload your SMS content to our servers.\n" +
                        "• Cloud Backups: If you enable Google Drive backups, we use your Google account to store an encrypted backup of your database in your own Google Drive. We do not have access to this data outside of the app's backup/restore functionality."
            )

            PolicySection(
                title = "3. Purpose of Processing",
                content = "We use your data to:\n" +
                        "• Provide personal finance tracking and analysis.\n" +
                        "• Automate transaction entry from SMS notifications.\n" +
                        "• Provide data backup and restoration services.\n" +
                        "• Improve app performance and user experience."
            )

            PolicySection(
                title = "4. Global Compliance",
                content = "• GDPR (EU): If you are from the European Economic Area, our legal basis for collecting and using the personal information depends on the Personal Information we collect and the specific context in which we collect it. You have the right to access, update, or delete your data.\n" +
                        "• India (IT Act): We comply with the Information Technology (Reasonable Security Practices and Procedures and Sensitive Personal Data or Information) Rules, 2011.\n" +
                        "• US (CCPA): We do not sell your personal information. We respect your rights to know what data is collected and to request its deletion."
            )

            PolicySection(
                title = "5. Data Security",
                content = "We implement industry-standard security measures to protect your data. Since most data is stored locally, its security also depends on your device's security (e.g., screen lock, biometric lock)."
            )

            PolicySection(
                title = "6. Developer & Contact Information",
                content = "For any queries regarding this Privacy Policy or data protection, please contact us at:\n\n" +
                        "Email: contact.zincstate@gmail.com\n" +
                        "Developer: Zincstate\n" +
                        "Location: India\n" +
                        "We will respond to all requests within 30 days."
            )
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )
    }
}
