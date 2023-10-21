package eu.matoosh.attendance.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import javax.inject.Inject

@HiltViewModel
class ConsoleViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    fun logout() {
        sessionManager.token = null
        sessionManager.saveSession()
    }
}