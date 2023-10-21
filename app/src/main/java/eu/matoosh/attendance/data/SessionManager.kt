package eu.matoosh.attendance.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.matoosh.attendance.R
import javax.inject.Inject
import javax.inject.Singleton

private const val USERNAME_KEY = "username"
private const val TOKEN_KEY = "token"
private const val VALIDITY_KEY = "validity"

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext val context: Context
) {
    var username: String? = null
    var token: String? = null
    var validity: String? = null

    fun saveSession() {
        val prefs = context.getSharedPreferences(
            context.resources.getString(R.string.preferences_file_key),
            Context.MODE_PRIVATE
        )
        prefs.edit()
            .putString(USERNAME_KEY, username)
            .putString(TOKEN_KEY, token)
            .putString(VALIDITY_KEY, validity)
            .apply()
    }

    fun restoreSession() {
        val prefs = context.getSharedPreferences(
            context.resources.getString(R.string.preferences_file_key),
            Context.MODE_PRIVATE
        )
        username = prefs.getString(USERNAME_KEY, null)
        token = prefs.getString(TOKEN_KEY, null)
        validity = prefs.getString(VALIDITY_KEY, null)
    }

    fun isAdmin() = username == "admin"
}