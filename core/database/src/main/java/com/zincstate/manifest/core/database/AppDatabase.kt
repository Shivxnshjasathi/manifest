package com.zincstate.manifest.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.BudgetDao
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.ContactDao
import com.zincstate.manifest.core.database.dao.GoalDao
import com.zincstate.manifest.core.database.dao.MonthlySummaryDao
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.dao.TransactionSplitDao
import com.zincstate.manifest.core.database.entity.AccountEntity
import com.zincstate.manifest.core.database.entity.BudgetEntity
import com.zincstate.manifest.core.database.entity.CategoryEntity
import com.zincstate.manifest.core.database.entity.ContactEntity
import com.zincstate.manifest.core.database.entity.GoalEntity
import com.zincstate.manifest.core.database.entity.MonthlySummaryEntity
import com.zincstate.manifest.core.database.entity.RecurringTransactionEntity
import com.zincstate.manifest.core.database.entity.TransactionEntity
import com.zincstate.manifest.core.database.entity.TransactionSplitEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        GoalEntity::class,
        RecurringTransactionEntity::class,
        MonthlySummaryEntity::class,
        ContactEntity::class,
        TransactionSplitEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun monthlySummaryDao(): MonthlySummaryDao
    abstract fun contactDao(): ContactDao
    abstract fun transactionSplitDao(): TransactionSplitDao

    companion object {
        const val DATABASE_NAME = "manifest_db"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(SeedDatabaseCallback())
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

/**
 * Prepopulates the database with default categories.
 */
private class SeedDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        seed(db)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        seed(db)
    }

    private fun seed(db: SupportSQLiteDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            // Check if categories exist
            val cursor = db.query("SELECT COUNT(*) FROM categories")
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            cursor.close()

            if (count == 0) {
                // Seed expense categories
                val expenseCategories = listOf(
                    Triple("Food", "🍜", "EXPENSE"),
                    Triple("Social Life", "🧑\u200D🤝\u200D🧑", "EXPENSE"),
                    Triple("Pets", "🐶", "EXPENSE"),
                    Triple("Transport", "🚕", "EXPENSE"),
                    Triple("Culture", "🖼️", "EXPENSE"),
                    Triple("Household", "🪑", "EXPENSE"),
                    Triple("Apparel", "👘", "EXPENSE"),
                    Triple("Beauty", "💄", "EXPENSE"),
                    Triple("Health", "🧘", "EXPENSE"),
                    Triple("Education", "📙", "EXPENSE"),
                    Triple("Gift", "🎁", "EXPENSE"),
                    Triple("Groceries", "🛒", "EXPENSE"),
                    Triple("Other", "📝", "EXPENSE"),
                )

                val incomeCategories = listOf(
                    Triple("Salary", "💰", "INCOME"),
                    Triple("Allowance", "💵", "INCOME"),
                    Triple("Bonus", "🎉", "INCOME"),
                    Triple("Petty Cash", "🪙", "INCOME"),
                    Triple("Other", "📝", "INCOME"),
                )

                (expenseCategories + incomeCategories).forEach { (name, icon, type) ->
                    db.execSQL(
                        "INSERT INTO categories (id, name, icon, type) VALUES (?, ?, ?, ?)",
                        arrayOf(UUID.randomUUID().toString(), name, icon, type)
                    )
                }
            }

            // Check if accounts exist
            val accCursor = db.query("SELECT COUNT(*) FROM accounts")
            accCursor.moveToFirst()
            val accCount = accCursor.getInt(0)
            accCursor.close()

            if (accCount == 0) {
                // Seed default Cash account
                db.execSQL(
                    "INSERT INTO accounts (id, name, `group`, balance, settlementDate, paymentDate) VALUES (?, ?, ?, ?, ?, ?)",
                    arrayOf(UUID.randomUUID().toString(), "Cash", "CASH", 0.0, 0, 0)
                )
            }
        }
    }
}
