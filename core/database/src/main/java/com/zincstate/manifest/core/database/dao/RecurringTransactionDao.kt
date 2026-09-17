package com.zincstate.manifest.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zincstate.manifest.core.database.entity.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {

    @Query("SELECT * FROM recurring_transactions ORDER BY nextRunDate")
    fun getAllRecurring(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE nextRunDate <= :today")
    suspend fun getDueRecurring(today: String): List<RecurringTransactionEntity>

    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    suspend fun getRecurringById(id: String): RecurringTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recurring: RecurringTransactionEntity)

    @Update
    suspend fun update(recurring: RecurringTransactionEntity)

    @Delete
    suspend fun delete(recurring: RecurringTransactionEntity)

    @Query("DELETE FROM recurring_transactions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM recurring_transactions")
    suspend fun deleteAll()
}
