package eu.matoosh.attendance.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.repo.LoginRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


sealed interface LoginUiState {
    data class Success(val token: String, val validity: String) : LoginUiState
    object Error : LoginUiState
    object Loading : LoginUiState
    object Idle : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: LoginRepository): ViewModel() {
    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginUiState = LoginUiState.Loading
            loginUiState = try {
                val session = repo.login(username, password)
                LoginUiState.Success(session.token, session.validity)
            } catch (e: IOException) {
                LoginUiState.Error
            } catch (e: HttpException) {
                LoginUiState.Error
            }
        }
    }
}