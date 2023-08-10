package eu.matoosh.attendance.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.LoginViewModel

@Composable
fun LoginScreenWrapper(
    onSuccess: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState = loginViewModel.loginUiState.collectAsState()
    LoginScreen(
        loginUiState,
        onSuccess = {
            onSuccess()
            loginViewModel.finish()
        },
        onLoginClick = {
                username, password -> loginViewModel.login(username, password)
        }
    )
}