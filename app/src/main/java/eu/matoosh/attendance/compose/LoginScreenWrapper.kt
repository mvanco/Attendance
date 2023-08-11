package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.viewmodels.LoginViewModel

@Composable
fun LoginScreenWrapper(
    onSuccess: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState = loginViewModel.loginUiState.collectAsState()
    LoginScreen(
        loginUiState,
        onSuccess = onSuccess,
        onLoginClick = {
                username, password -> loginViewModel.login(username, password)
        }
    )
}