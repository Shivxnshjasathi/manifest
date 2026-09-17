package com.zincstate.manifest.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zincstate.manifest.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // Paging3 source for daily view — keyset pagination on (date DESC, id DESC)
    @Query("""
        SELECT * FROM transactions 
        WHERE date LIKE :yearMonth || '%'
        ORDER BY date DESC, createdAt DESC
    """)
    fun getTransactionsByMonthPaged(yearMonth: String): PagingSource<Int, TransactionEntity>

    @Query("""
        SELECT * FROM transactions 
        WHERE date LIKE :yearMonth || '%'
        ORDER BY date DESC, createdAt DESC
    """)
    fun getTransactionsByMonth(yearMonth: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE accountId = :accountId
        ORDER BY date DESC, createdAt DESC
    """)
    fun getTransactionsByAccount(accountId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionByIdFlow(id: String): Flow<TransactionEntity?>

    // Search across all months
    @Query("""
        SELECT * FROM transactions 
        WHERE note LIKE '%' || :query || '%' 
           OR description LIKE '%' || :query || '%'
           OR categoryId IN (SELECT id FROM categories WHERE name LIKE '%' || :query || '%')
           OR accountId IN (SELECT id FROM accounts WHERE name LIKE '%' || :query || '%')
        ORDER BY date DESC, createdAt DESC
    """)
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    // Aggregation for monthly summary
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = 'INCOME' AND date LIKE :yearMonth || '%'
    """)
    suspend fun getTotalIncomeForMonth(yearMonth: String): Double

    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = 'EXPENSE' AND date LIKE :yearMonth || '%'
    """)
    suspend fun getTotalExpenseForMonth(yearMonth: String): Double

    // Transactions for a specific date
    @Query("""
        SELECT * FROM transactions 
        WHERE date = :date 
        ORDER BY createdAt DESC
    """)
    fun getTransactionsByDate(date: String): Flow<List<TransactionEntity>>

    // Dates that have transactions in a month (for calendar view)
    @Query("""
        SELECT DISTINCT date FROM transactions 
        WHERE date LIKE :yearMonth || '%'
    """)
    fun getTransactionDatesForMonth(yearMonth: String): Flow<List<String>>

    // Daily totals for a month
    @Query("""
        SELECT date, 
               SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as totalExpense,
               SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as totalIncome
        FROM transactions 
        WHERE date LIKE :yearMonth || '%'
        GROUP BY date
        ORDER BY date DESC
    """)
    fun getDailyTotalsForMonth(yearMonth: String): Flow<List<DailyTotal>>

    // Category breakdown for a month
    @Query("""
        SELECT categoryId, SUM(amount) as total 
        FROM transactions 
        WHERE type = :type AND date LIKE :yearMonth || '%'
        GROUP BY categoryId 
        ORDER BY total DESC
    """)
    fun getCategoryBreakdown(yearMonth: String, type: String): Flow<List<CategoryTotal>>

    // Count for stats
    @Query("SELECT COUNT(*) FROM transactions")
    fun getTransactionCount(): Flow<Int>

    // For NLP auto-categorization: find categories used with similar notes
    @Query("""
        SELECT categoryId, COUNT(*) as count 
        FROM transactions 
        WHERE note LIKE '%' || :noteKeyword || '%'
        GROUP BY categoryId 
        ORDER BY count DESC 
        LIMIT 5
    """)
    suspend fun getCategorySuggestionsForNote(noteKeyword: String): List<CategorySuggestion>

    // Last 30 days expenses for insights sparkline
    @Query("""
        SELECT date, SUM(amount) as totalExpense, 0.0 as totalIncome 
        FROM transactions 
        WHERE type = 'EXPENSE' AND date >= :startDate
        GROUP BY date 
        ORDER BY date ASC
    """)
    fun getDailyExpensesSince(startDate: String): Flow<List<DailyTotal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
    
    // Fuzzy import deduplication
    @Query("""
        SELECT COUNT(*) FROM transactions 
        WHERE date = :date AND amount = :amount 
          AND (note LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%')
    """)
    suspend fun checkFuzzyDuplicate(date: String, amount: Double, keyword: String): Int
    
    // Autocomplete suggestions for notes
    @Query("""
        SELECT DISTINCT note FROM transactions
        WHERE note LIKE :query || '%' AND note != ''
        ORDER BY createdAt DESC
        LIMIT 5
    """)
    suspend fun getNoteSuggestions(query: String): List<String>
}

data class DailyTotal(
    val date: String,
    val totalExpense: Double,
    val totalIncome: Double
)

data class CategoryTotal(
    val categoryId: String,
    val total: Double
)

data class CategorySuggestion(
    val categoryId: String,
    val count: Int
)
