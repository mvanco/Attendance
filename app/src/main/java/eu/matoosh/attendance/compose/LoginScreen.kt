package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.LoginViewModel
import kotlinx.coroutines.flow.StateFlow

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
            onSuccess()
        }
        is LoginUiState.Logged -> {
            Message("Admin je úspěšně přihlášen.")
        }
    }
}