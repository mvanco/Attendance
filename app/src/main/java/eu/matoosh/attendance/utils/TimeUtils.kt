package eu.matoosh.attendance.utils

import eu.matoosh.attendance.config.DATE_FORMAT_PATTERN_APP
import eu.matoosh.attendance.config.DATE_FORMAT_PATTERN_BACKEND
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val backendFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN_BACKEND, Locale.US)
val frontendFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN_APP, Locale.US)

fun String.toDateTime(formatter: DateTimeFormatter = backendFormatter): ZonedDateTime = ZonedDateTime.parse(this, formatter)
fun ZonedDateTime.toFormattedString(formatter: DateTimeFormatter = backendFormatter): String = formatter.format(this)