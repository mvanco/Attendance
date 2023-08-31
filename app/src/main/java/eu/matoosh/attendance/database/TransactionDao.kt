package eu.matoosh.attendance.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    fun getAll(): Flow<List<Transaction>>

    @Insert
    fun insert(transaction: Transaction)
}