package eu.matoosh.attendance.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.TOKEN_KEY
import eu.matoosh.attendance.data.USERNAME_KEY
import eu.matoosh.attendance.data.VALIDITY_KEY
import javax.inject.Inject

@HiltViewModel
class ConsoleViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        if (sessionManager.token == null) {
            restoreState()
        }
        else {
            if (
                sessionManager.username != "admin"
                && sessionManager.username?.startsWith("admin_") == false)
            saveState()
        }
    }

    private fun saveState() {
        savedStateHandle[USERNAME_KEY] = sessionManager.username
        savedStateHandle[TOKEN_KEY] = sessionManager.token
        savedStateHandle[VALIDITY_KEY] = sessionManager.validity
    }

    private fun restoreState() {
        sessionManager.username = savedStateHandle[USERNAME_KEY]
        sessionManager.token = savedStateHandle[TOKEN_KEY]
        sessionManager.validity = savedStateHandle[VALIDITY_KEY]
    }

    fun logout() {
        sessionManager.token = null
        sessionManager.saveSession()
    }
}