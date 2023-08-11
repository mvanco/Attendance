package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import eu.matoosh.attendance.viewmodels.LoginUiState
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    loginUiState: State<LoginUiState>,
    onLoginClick: (String, String) -> Unit,
    onSuccess: () -> Unit
) {
    when (loginUiState.value) {
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