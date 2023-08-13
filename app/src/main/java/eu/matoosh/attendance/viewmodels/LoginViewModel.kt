package eu.matoosh.attendance.viewmodels

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.LoginRepository
import eu.matoosh.attendance.repo.RepoLoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@Stable
sealed interface LoginUiState {
    data class Error(val message: String) : LoginUiState
    object Success : LoginUiState
    object Loading : LoginUiState
    object Idle : LoginUiState

    companion object {
        const val SUCCESS_STATE_DURATION = 1000L
    }
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    init {
        Log.d("LoginViewModel", "LoginViewModel initiaized")
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                when (val response = repo.login(username, password)) {
                    is RepoLoginResponse.Success -> {
                        sessionManager.token = response.token
                        sessionManager.validity = response.validity
                        _loginUiState.value = LoginUiState.Success
                    }
                    is RepoLoginResponse.Error -> {
                        _loginUiState.value = LoginUiState.Error(response.message)
                    }
                }
            } catch (e: IOException) {
                LoginUiState.Error("IOException")
            } catch (e: HttpException) {
                LoginUiState.Error("HttpException")
            }
        }
    }

    fun logout() {
        sessionManager.token = ""
        sessionManager.validity = ""
        _loginUiState.value = LoginUiState.Idle
    }
}