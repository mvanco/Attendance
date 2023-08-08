package eu.matoosh.attendance.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.LoginRepository
import eu.matoosh.attendance.repo.RepoLoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


sealed interface LoginUiState {
    data class Success(val token: String, val validity: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
    object Loading : LoginUiState
    object Idle : LoginUiState
    object Finished : LoginUiState
    object None : LoginUiState
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState>
        get() = _loginUiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                when (val response = repo.login(username, password)) {
                    is RepoLoginResponse.Success -> {
                        sessionManager.token = response.token
                        sessionManager.validity = response.validity
                        _loginUiState.value = LoginUiState.Success(
                            response.token,
                            response.validity
                        )
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

    override fun onCleared() {
        super.onCleared()
    }

}