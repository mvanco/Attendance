package eu.matoosh.attendance.viewmodels.console

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.CreditRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
sealed interface AdminCreditsUiState {
    object Form : AdminCreditsUiState
    data class Qr(val authToken: String, val counter: Int) : AdminCreditsUiState
}

@HiltViewModel
class AdminCreditsViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val creditRepo: CreditRepository
) : ViewModel() {

    private val _adminCreditsUiState =
        MutableStateFlow<AdminCreditsUiState>(AdminCreditsUiState.Form)
    val adminCreditsUiState = _adminCreditsUiState.asStateFlow()
    private var countdownJob: Job? = null

    fun showQr(credit: Int) {
        countdownJob = viewModelScope.launch {
            if (sessionManager.token == null) {
                return@launch
            }
            val response = creditRepo.issueAuthorization(sessionManager.token!!, credit)
            for (counter in 10 downTo 1) {
                _adminCreditsUiState.value = AdminCreditsUiState.Qr(response.authToken, counter)
                delay(1000)
            }
            _adminCreditsUiState.value = AdminCreditsUiState.Form
        }
    }

    fun showForm() {
        countdownJob?.cancel()
        _adminCreditsUiState.value = AdminCreditsUiState.Form
    }
}