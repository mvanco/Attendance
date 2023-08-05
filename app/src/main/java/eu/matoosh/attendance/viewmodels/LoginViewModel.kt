package eu.matoosh.attendance.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.LoginRepository
import eu.matoosh.attendance.repo.RepoLoginResponse
import eu.matoosh.attendance.utils.ImmutableLiveData
import eu.matoosh.attendance.utils.asImmutable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


sealed interface LoginUiState {
    data class Success(val token: String, val validity: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
    object Loading : LoginUiState
    object Idle : LoginUiState
}


@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: LoginRepository) : ViewModel() {
    private val _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState = _loginUiState.asImmutable()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            _loginUiState.value = try {
                when (val response = repo.login(username, password)) {
                    is RepoLoginResponse.Success -> LoginUiState.Success(
                        response.token,
                        response.validity
                    )

                    is RepoLoginResponse.Error -> LoginUiState.Error(response.message)
                }
            } catch (e: IOException) {
                LoginUiState.Error("IOException")
            } catch (e: HttpException) {
                LoginUiState.Error("HttpException")
            }
        }
    }
}