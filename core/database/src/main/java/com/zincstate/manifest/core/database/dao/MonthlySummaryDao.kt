package com.zincstate.manifest.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zincstate.manifest.core.database.entity.MonthlySummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlySummaryDao {

    @Query("SELECT * FROM monthly_summaries WHERE yearMonth = :yearMonth")
    fun getSummaryForMonth(yearMonth: String): Flow<MonthlySummaryEntity?>

    @Query("SELECT * FROM monthly_summaries WHERE yearMonth = :yearMonth")
    suspend fun getSummaryForMonthSync(yearMonth: String): MonthlySummaryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(summary: MonthlySummaryEntity)

    @Query("DELETE FROM monthly_summaries")
    suspend fun deleteAll()
}
