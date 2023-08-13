package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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
    onSuccess: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    LoginScreen(
        loginUiState,
        onSuccess = onSuccess,
        onLoginClick = {
                username, password -> loginViewModel.login(username, password)
        }
    )
}

@Composable
fun LoginScreen(
    loginUiState: LoginUiState,
    onLoginClick: (String, String) -> Unit,
    onSuccess: () -> Unit
) {
    when (loginUiState) {
        is LoginUiState.Error -> {
            Message("Nastala chyba při přihlašování. :(")
        }
        is LoginUiState.Idle -> {
            LoginForm(onLoginClick)
        }
        is LoginUiState.Loading -> {
            Message("Přihlašuji...")
        }
        is LoginUiState.Success -> {
            Message("Admin je úspěšně přihlášen.")
            LaunchedEffect(Unit) {
                delay(LoginUiState.SUCCESS_STATE_DURATION)
                onSuccess()
            }
        }
    }
}