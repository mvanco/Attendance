package eu.matoosh.attendance.viewmodels.console

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.config.FORCED_LOADING_TIMOUT_MS
import eu.matoosh.attendance.data.Interest
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.ConsoleRepository
import eu.matoosh.attendance.repo.RepoConsoleErrorCode
import eu.matoosh.attendance.repo.RepoInterestsResponse
import eu.matoosh.attendance.repo.RepoRegisterTermResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
enum class UserTermsErrorCode() {
    INSUFFICIENT_CREDIT,
    INVALID_TOKEN,
    UNKNOWN_ERROR
}

@Stable
sealed interface UserTermsUiState {
    object Loading: UserTermsUiState
    data class Idle(val interests: List<Interest>): UserTermsUiState
    data class Error(val errorCode: UserTermsErrorCode): UserTermsUiState
    data class NewInterest(val interests: List<Interest>, val interestsToReg: List<Interest>): UserTermsUiState
}

@HiltViewModel
class UserTermsViewModel @Inject constructor(
    val consoleRepo: ConsoleRepository,
    val sessionManager: SessionManager
): ViewModel() {

    private val _uiState = MutableStateFlow<UserTermsUiState>(UserTermsUiState.Loading)
    private val _uiStateImmediate = MutableStateFlow<UserTermsUiState>(UserTermsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _registrationEnabled = MutableStateFlow(false)
    val registrationEnabled = _registrationEnabled.asStateFlow()

    private val terms = MutableStateFlow<List<Interest>>(emptyList())

    init {
        loadTerms()
    }

    fun loadTerms(useLoader: Boolean = false) {
        viewModelScope.launch {
            if (useLoader) {
                _uiState.value = UserTermsUiState.Loading
                delay(FORCED_LOADING_TIMOUT_MS)
            }
            _loadTerms()
            _uiState.value = UserTermsUiState.Idle(
                terms.value.filter { it.registered }
            )
            _registrationEnabled.value = terms.value.any { !it.registered }
        }
    }

    private suspend fun _loadTerms() {
        val result = consoleRepo.interests(sessionManager.token!!)
        when (result) {
            is RepoInterestsResponse.Success -> {
                terms.value = result.interests

            }
            is RepoInterestsResponse.Error -> {
                _uiState.value = UserTermsUiState.Error(UserTermsErrorCode.UNKNOWN_ERROR)
            }
        }
    }

    fun selectTerm() {
        _uiState.value = UserTermsUiState.NewInterest(
            interests = terms.value.filter { it.registered },
            interestsToReg = terms.value.filter { !it.registered }
        )
    }

    fun dismiss() {
        _uiState.value = UserTermsUiState.Idle(
            terms.value.filter { it.registered }
        )
        _registrationEnabled.value = terms.value.any { !it.registered }
    }

    fun register(id: Int) {
        viewModelScope.launch {
            val result = consoleRepo.registeTerm(sessionManager.token!!, id)
            when (result) {
                is RepoRegisterTermResponse.Success -> {
                    _loadTerms()
                    _uiState.value = UserTermsUiState.Idle(
                        terms.value.filter { it.registered }
                    )
                    _registrationEnabled.value = terms.value.any { !it.registered }
                }
                is RepoRegisterTermResponse.Error -> {
                    when (result.error) {
                        RepoConsoleErrorCode.INSUFFICIENT_CREDIT -> {
                            _uiState.value = UserTermsUiState.Error(UserTermsErrorCode.INSUFFICIENT_CREDIT)
                        }
                        RepoConsoleErrorCode.INVALID_TOKEN -> {
                            _uiState.value = UserTermsUiState.Error(UserTermsErrorCode.INVALID_TOKEN)
                        }
                        RepoConsoleErrorCode.UNKNOWN_ERROR -> {
                            _uiState.value = UserTermsUiState.Error(UserTermsErrorCode.UNKNOWN_ERROR)
                        }
                    }
                }
            }
        }
    }
}