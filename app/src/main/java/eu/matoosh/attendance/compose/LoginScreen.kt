package eu.matoosh.attendance.compose

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import eu.matoosh.attendance.viewmodels.LoginViewModel
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.BookViewModel
import eu.matoosh.attendance.viewmodels.LoginUiState

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    bookViewModel: BookViewModel,
) {
    val loginUiState = loginViewModel.loginUiState.observeAsState(LoginUiState.Idle)

    when (loginUiState.value) {
        is LoginUiState.Error -> {
            Text("Failure in LoginScreen")
        }
        is LoginUiState.Finished -> {
            Text("LoginScreen has finished")
        }
        is LoginUiState.Idle -> {
            LoginForm(loginViewModel)
        }
        is LoginUiState.Loading -> {
            Message("Přihlašuji...")
        }
        is LoginUiState.Success -> {
            loginViewModel.finish()
            bookViewModel.loadUsers()
            navController.navigate("attendance_sheet")
        }
    }
}