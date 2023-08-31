package eu.matoosh.attendance.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

enum class Operation {
    WITHDRAWAL,
    DEPOSIT
}

@Entity(tableName = "transaction")
data class Transaction(
    @ColumnInfo(name = "date_time") val dateTime: Calendar = Calendar.getInstance(),
    val user: Int,
    val operation: String,
    val relative: Int,
    val absolute: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
