package eu.matoosh.attendance.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState : LiveData<LoginUiState> get() = _loginUiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            _loginUiState.value = try {
                val session = repo.login(username, password)
                if (session.token != "N/A") {
                    LoginUiState.Success(session.token, session.validity)
                }
                else {
                    LoginUiState.Error
                }
            } catch (e: IOException) {
                LoginUiState.Error
            } catch (e: HttpException) {
                LoginUiState.Error
            }
        }
    }
}