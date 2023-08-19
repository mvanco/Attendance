package eu.matoosh.attendance.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    var username: String? = null
    var token: String? = null
    var validity: String? = null
}