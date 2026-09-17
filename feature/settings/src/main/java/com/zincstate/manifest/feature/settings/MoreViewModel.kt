package com.zincstate.manifest.feature.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.BudgetDao
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.GoalDao
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val goalDao: GoalDao,
    private val recurringDao: RecurringTransactionDao,
    private val monthlySummaryDao: MonthlySummaryDao,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    val isDarkTheme: Flow<Boolean> = preferencesDataStore.isDarkTheme
    val dynamicColor: Flow<Boolean> = preferencesDataStore.dynamicColor
    val transactionCount: Flow<Int> = transactionDao.getTransactionCount()
    val accountCount: Flow<Int> = accountDao.getAccountCount()

    fun toggleTheme() {
        viewModelScope.launch {
            preferencesDataStore.toggleDarkTheme()
        }
    }

    fun toggleDynamicColor() {
        viewModelScope.launch {
            preferencesDataStore.toggleDynamicColor()
        }
    }

    fun exportCsv() {
        // TODO: Phase 4 — CSV export via Storage Access Framework
    }

    fun clearAllData() {
        // TODO: Show confirmation dialog first
        viewModelScope.launch {
            transactionDao.deleteAll()
            accountDao.deleteAll()
            budgetDao.deleteAll()
            goalDao.deleteAll()
            recurringDao.deleteAll()
            monthlySummaryDao.deleteAll()
        }
    }

    fun backupDatabase(context: android.content.Context, uri: android.net.Uri) {
        viewModelScope.launch {
            val dbFile = context.getDatabasePath("manifest_db")
            if (dbFile.exists()) {
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    dbFile.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}
