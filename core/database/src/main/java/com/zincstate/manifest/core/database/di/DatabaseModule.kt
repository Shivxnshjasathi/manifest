package com.zincstate.manifest.core.database.di

import android.content.Context
import com.zincstate.manifest.core.database.AppDatabase
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.BudgetDao
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.ContactDao
import com.zincstate.manifest.core.database.dao.GoalDao
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.dao.TransactionSplitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao =
        database.transactionDao()

    @Provides
    fun provideAccountDao(database: AppDatabase): AccountDao =
        database.accountDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao =
        database.categoryDao()

    @Provides
    fun provideBudgetDao(database: AppDatabase): BudgetDao =
        database.budgetDao()

    @Provides
    fun provideGoalDao(database: AppDatabase): GoalDao =
        database.goalDao()

    @Provides
    fun provideRecurringTransactionDao(database: AppDatabase): RecurringTransactionDao =
        database.recurringTransactionDao()

    @Provides
    fun provideMonthlySummaryDao(database: AppDatabase): MonthlySummaryDao =
        database.monthlySummaryDao()

    @Provides
    fun provideContactDao(database: AppDatabase): ContactDao =
        database.contactDao()

    @Provides
    fun provideTransactionSplitDao(database: AppDatabase): TransactionSplitDao =
        database.transactionSplitDao()
}
