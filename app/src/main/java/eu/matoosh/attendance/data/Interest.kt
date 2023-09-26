package eu.matoosh.attendance.data

import java.util.Date

data class Interest(
    val duration: Int,
    val price: Int,
    val rentalId: Int,
    val start: Date
)
