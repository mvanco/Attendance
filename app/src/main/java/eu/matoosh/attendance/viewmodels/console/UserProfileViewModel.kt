package eu.matoosh.attendance.viewmodels.console

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.config.FORCED_LOADING_TIMOUT_MS
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.repo.ConsoleRepository
import eu.matoosh.attendance.repo.RepoProfileResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProfileErrorCode() {
    UNKNOWN_ERROR
}

sealed interface UserProfileUiState {
    data class Idle(val user: User) : UserProfileUiState
    object Loading : UserProfileUiState
    data class Error(val errorCode: ProfileErrorCode) : UserProfileUiState
}

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val consoleRepo: ConsoleRepository
) : ViewModel() {
    private val _userProfileUiState =
        MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val userProfileUiState = _userProfileUiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile(useLoader: Boolean = false) {
        viewModelScope.launch {
            if (useLoader) {
                _userProfileUiState.value = UserProfileUiState.Loading
                delay(FORCED_LOADING_TIMOUT_MS)
            }
            when (val response = consoleRepo.profile(sessionManager.token!!)) {
                is RepoProfileResponse.Success -> {
                    _userProfileUiState.value = UserProfileUiState.Idle(response.profile)
                }
                else -> {
                    _userProfileUiState.value = UserProfileUiState.Error(ProfileErrorCode.UNKNOWN_ERROR)
                }
            }
        }
    }
}