package com.zincstate.manifest.core.database.dao

import androidx.room.*
import com.zincstate.manifest.core.database.entity.TransactionSplitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionSplitDao {
    @Query("SELECT * FROM transaction_splits WHERE transactionId = :transactionId")
    fun getSplitsForTransaction(transactionId: String): Flow<List<TransactionSplitEntity>>

    @Query("SELECT * FROM transaction_splits WHERE contactId = :contactId AND isSettled = 0")
    fun getUnsettledSplitsForContact(contactId: String): Flow<List<TransactionSplitEntity>>

    @Query("SELECT * FROM transaction_splits WHERE transactionId IN (:transactionIds)")
    fun getSplitsForTransactions(transactionIds: List<String>): Flow<List<TransactionSplitEntity>>

    @Query("""
        SELECT ts.* FROM transaction_splits ts
        INNER JOIN transactions t ON ts.transactionId = t.id
        WHERE t.date LIKE :yearMonth || '%'
    """)
    fun getSplitsForTransactionsByMonth(yearMonth: String): Flow<List<TransactionSplitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplits(splits: List<TransactionSplitEntity>)

    @Query("UPDATE transaction_splits SET isSettled = 1 WHERE id = :splitId")
    suspend fun settleSplit(splitId: String)

    @Query("SELECT SUM(amount) FROM transaction_splits WHERE contactId = :contactId AND isSettled = 0")
    fun getOwedAmountForContact(contactId: String): Flow<Double?>

    @Delete
    suspend fun delete(split: TransactionSplitEntity)
}
