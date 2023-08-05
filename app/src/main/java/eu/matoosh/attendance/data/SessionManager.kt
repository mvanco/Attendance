package eu.matoosh.attendance.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    var token: String? = null
}