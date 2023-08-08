package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val loginUiState = loginViewModel.loginUiState.collectAsState()

    when (loginUiState.value) {
        is LoginUiState.Error -> {
            Message("Nastala chyba při přihlašování. :(")
        }
        is LoginUiState.Finished -> {
            Message("Admin je úspěšně přihlášen.")
        }
        is LoginUiState.Idle -> {
            LoginForm(loginViewModel)
        }
        is LoginUiState.Loading -> {
            Message("Přihlašuji...")
        }
        is LoginUiState.Success -> {
            LaunchedEffect(key1 = true) {
                navController.navigate("attendance_sheet")
            }
        }
        is LoginUiState.None -> {}
    }
}