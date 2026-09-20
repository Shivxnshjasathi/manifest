package com.zincstate.manifest

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.entity.TransactionEntity
import com.zincstate.manifest.core.datastore.UserPreferencesDataStore
import com.zincstate.manifest.core.ui.theme.ManifestTheme
import com.zincstate.manifest.navigation.ManifestNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zincstate.manifest.work.RecurringWorker
import com.zincstate.manifest.work.ReminderWorker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.concurrent.TimeUnit
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.zincstate.manifest.R

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var preferencesDataStore: UserPreferencesDataStore
    
    @Inject
    lateinit var recurringDao: RecurringTransactionDao
    
    @Inject
    lateinit var transactionDao: TransactionDao

    @Inject
    lateinit var accountDao: AccountDao

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Enqueue recurring worker (as a fallback)
        val workRequest = PeriodicWorkRequestBuilder<RecurringWorker>(1, TimeUnit.DAYS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "recurring_transactions",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        
        setupAppShortcuts()
        scheduleReminder()
        checkBiometricLock()
        
        // Sync catch-up for subscriptions
        lifecycleScope.launch(Dispatchers.IO) {
            val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            try {
                val dueItems = recurringDao.getDueRecurring(todayStr)
                if (dueItems.isNotEmpty()) {
                    val newTransactions = mutableListOf<TransactionEntity>()
                    for (recurring in dueItems) {
                        var currentRunDate = LocalDate.parse(recurring.nextRunDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        val today = LocalDate.now()
                        var iterations = 0
                        while (!currentRunDate.isAfter(today) && iterations < 365) {
                            val tx = TransactionEntity(
                                id = UUID.randomUUID().toString(),
                                type = recurring.type,
                                amount = recurring.amount,
                                categoryId = recurring.categoryId,
                                accountId = recurring.accountId,
                                toAccountId = recurring.toAccountId,
                                note = recurring.note,
                                description = recurring.description,
                                superCategory = recurring.superCategory,
                                date = currentRunDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                createdAt = System.currentTimeMillis(),
                                recurringId = recurring.id
                            )
                            newTransactions.add(tx)
                            
                            // Apply impact to balance
                            when (tx.type) {
                                "INCOME" -> accountDao.updateBalance(tx.accountId, tx.amount)
                                "EXPENSE" -> accountDao.updateBalance(tx.accountId, -tx.amount)
                                "TRANSFER" -> {
                                    accountDao.updateBalance(tx.accountId, -tx.amount)
                                    tx.toAccountId?.let { accountDao.updateBalance(it, tx.amount) }
                                }
                            }

                            currentRunDate = when (recurring.frequency) {
                                "DAILY" -> currentRunDate.plusDays(1)
                                "WEEKLY" -> currentRunDate.plusWeeks(1)
                                "MONTHLY" -> currentRunDate.plusMonths(1)
                                "YEARLY" -> currentRunDate.plusYears(1)
                                else -> currentRunDate.plusMonths(1)
                            }
                            iterations++
                        }
                        recurringDao.update(
                            recurring.copy(nextRunDate = currentRunDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        )
                    }
                    if (newTransactions.isNotEmpty()) {
                        transactionDao.insertAll(newTransactions)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Read initial theme synchronously to prevent UI flash
        val initialDarkTheme = kotlinx.coroutines.runBlocking {
            preferencesDataStore.isDarkTheme.first()
        }

        setContent {
            val isDarkTheme by preferencesDataStore.isDarkTheme.collectAsState(initial = initialDarkTheme)
            val dynamicColor by preferencesDataStore.dynamicColor.collectAsState(initial = true)

            androidx.compose.runtime.DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = if (isDarkTheme) {
                        androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        androidx.activity.SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
                    },
                    navigationBarStyle = if (isDarkTheme) {
                        androidx.activity.SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        androidx.activity.SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
                    }
                )
                
                // Also set window background to prevent white flash during transition
                window.decorView.setBackgroundColor(
                    if (isDarkTheme) android.graphics.Color.parseColor("#0F0F13") // Dark background color
                    else android.graphics.Color.parseColor("#F5F5F7") // Light background color
                )
                
                onDispose {}
            }

            ManifestTheme(darkTheme = isDarkTheme, dynamicColor = dynamicColor) {
                val isQuickAdd = intent?.action == "com.zincstate.manifest.ACTION_QUICK_ADD"
                ManifestNavGraph(isQuickAdd = isQuickAdd)
            }
        }
    }

    private fun setupAppShortcuts() {
        val shortcut = ShortcutInfoCompat.Builder(this, "add_transaction")
            .setShortLabel("Add Transaction")
            .setLongLabel("Add a new transaction")
            .setIcon(IconCompat.createWithResource(this, R.drawable.ic_launcher_foreground))
            .setIntent(
                Intent(this, MainActivity::class.java).apply {
                    action = "com.zincstate.manifest.ACTION_QUICK_ADD"
                }
            )
            .build()

        ShortcutManagerCompat.addDynamicShortcuts(this, listOf(shortcut))
    }

    private fun scheduleReminder() {
        val now = LocalDateTime.now()
        var target = now.with(LocalTime.of(22, 0)) // 10 PM
        if (now.isAfter(target)) target = target.plusDays(1)

        val delay = ChronoUnit.MILLIS.between(now, target)

        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_expense_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )
    }

    private fun checkBiometricLock() {
        lifecycleScope.launch {
            val isLocked = preferencesDataStore.isBiometricLocked.first()
            if (isLocked) {
                showBiometricPrompt()
            }
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    finish() // Close app if authentication fails/canceled
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Manifest")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}