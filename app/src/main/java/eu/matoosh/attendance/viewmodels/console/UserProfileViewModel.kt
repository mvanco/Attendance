package eu.matoosh.attendance.viewmodels.console

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.config.FORCED_LOADING_TIMOUT_MS
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.TOKEN_KEY
import eu.matoosh.attendance.data.USERNAME_KEY
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.data.VALIDITY_KEY
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
    private val consoleRepo: ConsoleRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _userProfileUiState =
        MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val userProfileUiState = _userProfileUiState.asStateFlow()

    init {
        if (sessionManager.token == null) {
            restoreState()
        }
        else {
            saveState()
        }
        loadProfile()
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