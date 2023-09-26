package eu.matoosh.attendance.data

import java.time.ZonedDateTime

data class Interest(
    val duration: Int,
    val price: Int,
    val rentalId: Int,
    val start: ZonedDateTime,
    val registered: Boolean
)
