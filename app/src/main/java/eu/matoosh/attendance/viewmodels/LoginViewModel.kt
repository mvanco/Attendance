package eu.matoosh.attendance.viewmodels

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.Device
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.LoginRepository
import eu.matoosh.attendance.repo.RepoLoginErrorCode
import eu.matoosh.attendance.repo.RepoLoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

enum class LoginErrorCode() {
    INCORRECT_USERNAME,
    INCORRECT_PASSWORD,
    UNKNOWN_ERROR
}

@Stable
sealed interface LoginUiState {
    data class Error(val errorCode: LoginErrorCode) : LoginUiState
    data class Success(val username: String) : LoginUiState
    object Idle : LoginUiState
    object Loading : LoginUiState

    companion object {
        const val SUCCESS_STATE_DURATION = 1000L
        const val FAILURE_STATE_DURATION = 1000L
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository,
    private val sessionManager: SessionManager,
    private val device: Device,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    init {
        username.value = sessionManager.username ?: ""
    }

    fun initialize(newUsername: String, newPassword: String) {
        sessionManager.token = ""
        sessionManager.validity = ""
        username.value = newUsername
        password.value = newPassword
        _loginUiState.value = LoginUiState.Idle
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                when (val response = repo.login(username, password)) {
                    is RepoLoginResponse.Success -> {
                        sessionManager.username = username
                        sessionManager.token = response.token
                        sessionManager.validity = response.validity
                        _loginUiState.value = LoginUiState.Success(username)
                    }
                    is RepoLoginResponse.Error -> {
                        val errorCode = when(response.error) {
                            RepoLoginErrorCode.INCORRECT_USERNAME -> LoginErrorCode.INCORRECT_USERNAME
                            RepoLoginErrorCode.INCORRECT_PASSWORD -> LoginErrorCode.INCORRECT_PASSWORD
                            else -> LoginErrorCode.UNKNOWN_ERROR
                        }
                        _loginUiState.value = LoginUiState.Error(errorCode)
                    }
                }
            } catch (e: IOException) {
                _loginUiState.value = LoginUiState.Error(LoginErrorCode.UNKNOWN_ERROR)
            } catch (e: HttpException) {
                _loginUiState.value = LoginUiState.Error(LoginErrorCode.UNKNOWN_ERROR)
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error(LoginErrorCode.UNKNOWN_ERROR)
            }
        }
    }
}