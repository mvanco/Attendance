package eu.matoosh.attendance.viewmodels.console

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.config.UI_DEBOUNCE_TIMEOUT_SHORT_MS
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.CreditRepository
import eu.matoosh.attendance.repo.RepoCreditErrorCode
import eu.matoosh.attendance.repo.RepoCreditResponse
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
sealed interface UserScannerUiState {
    object Scanner : UserScannerUiState
    object Success : UserScannerUiState
    data class Error(val error: UserScannerErrorCode) : UserScannerUiState
}

@Stable
enum class UserScannerErrorCode {
    WRONG_USER,
    UNKNOWN_ERROR
}

@OptIn(FlowPreview::class)
@HiltViewModel
class UserScannerViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val creditRepo: CreditRepository
) : ViewModel() {
    private val _userScannerUiState =
        MutableStateFlow<UserScannerUiState>(UserScannerUiState.Scanner)
    val userScannerUiState = _userScannerUiState.debounce(UI_DEBOUNCE_TIMEOUT_SHORT_MS).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UserScannerUiState.Scanner
    )

    fun initialize() {
        _userScannerUiState.value = UserScannerUiState.Scanner
    }

    fun addCredit(authToken: String) {
        viewModelScope.launch {
            if (sessionManager.token == null) {
                return@launch
            }
            when (val response = creditRepo.addCredit(sessionManager.token!!, authToken)) {
                is RepoCreditResponse.Success -> {
                    _userScannerUiState.value = UserScannerUiState.Success
                }
                is RepoCreditResponse.Error -> {
                    when (response.error) {
                        RepoCreditErrorCode.WRONG_USER -> {
                            _userScannerUiState.value = UserScannerUiState.Error(UserScannerErrorCode.WRONG_USER)
                        }
                        RepoCreditErrorCode.UNKNOWN -> {
                            _userScannerUiState.value = UserScannerUiState.Error(UserScannerErrorCode.UNKNOWN_ERROR)
                        }
                    }
                }
            }
        }
    }
}