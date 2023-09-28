package eu.matoosh.attendance.viewmodels.console

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.CreditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
sealed interface UserScannerUiState {
    object Scanner : UserScannerUiState
    object Success : UserScannerUiState
}

@HiltViewModel
class UserScannerViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val creditRepo: CreditRepository
) : ViewModel() {
    private val _userScannerUiState =
        MutableStateFlow<UserScannerUiState>(UserScannerUiState.Scanner)
    val userScannerUiState = _userScannerUiState.asStateFlow()

    fun initialize() {
        _userScannerUiState.value = UserScannerUiState.Scanner
    }

    fun addCredit(authToken: String) {
        viewModelScope.launch {
            if (sessionManager.token == null) {
                return@launch
            }
            creditRepo.addCredit(sessionManager.token!!, authToken)
            _userScannerUiState.value = UserScannerUiState.Success
        }
    }
}