package com.zincstate.manifest.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zincstate.manifest.core.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts ORDER BY `group`, name")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: String): AccountEntity?

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountByIdFlow(id: String): Flow<AccountEntity?>

    @Query("SELECT COUNT(*) FROM accounts")
    fun getAccountCount(): Flow<Int>

    @Query("SELECT * FROM accounts WHERE name = :name LIMIT 1")
    suspend fun getAccountByName(name: String): AccountEntity?

    @Query("UPDATE accounts SET balance = balance + :delta WHERE id = :id")
    suspend fun updateBalance(id: String, delta: Double)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()
}
