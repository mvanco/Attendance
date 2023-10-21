package eu.matoosh.attendance.compose.screen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.widget.global.FullScreenMessage
import eu.matoosh.attendance.compose.widget.global.LoginForm
import eu.matoosh.attendance.viewmodels.LoginErrorCode
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.LoginViewModel
import kotlinx.coroutines.delay

/**
 * Login screen that takes username and password and log user storing obtained token into
 * SessionManager.
 * @param onSuccess What should be performed after user is sucessfully logged in.
 * @param loginViewModel View model linked directly to this screen providing business logic and
 * connection to REST service.
 */
@Composable
fun LoginScreen(
    onSuccess: (String) -> Unit,
    onFailure: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreen(
        loginViewModel,
        onSuccess = onSuccess,
        onFailure = onFailure,
        onLoginClick = {
                username, password -> loginViewModel.login(username, password)
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: (String, String) -> Unit,
    onSuccess: (String) -> Unit,
    onFailure: () -> Unit
) {
    val uiState by loginViewModel.loginUiState.collectAsState()
    when (uiState) {
        is LoginUiState.Error -> {
            when((uiState as LoginUiState.Error).errorCode) {
                LoginErrorCode.INCORRECT_USERNAME -> {
                    FullScreenMessage(stringResource(R.string.message_logging_error_incorrect_username))
                }
                LoginErrorCode.INCORRECT_PASSWORD -> {
                    FullScreenMessage(stringResource(R.string.message_logging_error_incorrect_password))
                }
                LoginErrorCode.UNKNOWN_ERROR -> {
                    FullScreenMessage(stringResource(R.string.message_logging_error))
                }
            }
            LaunchedEffect(Unit) {
                delay(LoginUiState.FAILURE_STATE_DURATION)
                onFailure()
            }
        }
        is LoginUiState.Idle -> {
            val userState = loginViewModel.username.collectAsState() as MutableState
            val passState = loginViewModel.password.collectAsState() as MutableState
            LoginForm(
                onLoginClick,
                userState,
                passState
            )
        }
        is LoginUiState.Loading -> {
            FullScreenMessage(stringResource(id = R.string.message_logging_loading))
        }
        is LoginUiState.Success -> {
            FullScreenMessage(stringResource(R.string.message_logging_success, (uiState as LoginUiState.Success).username.capitalize()))
            LaunchedEffect(Unit) {
                delay(LoginUiState.SUCCESS_STATE_DURATION)
                onSuccess((uiState as LoginUiState.Success).username)
            }
        }
    }
}