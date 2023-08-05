package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.matoosh.attendance.Greeting
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.LoginViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun AttendanceNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUiState = loginViewModel.loginUiState.observeAsState(LoginUiState.Idle)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {

            LoginForm ({
            }, loginViewModel)
        }
        composable("success") {
            Greeting("Success")
        }
        composable("failure") {
            Greeting("Failure")
        }
    }

    when (loginUiState.value) {
        is LoginUiState.Idle, LoginUiState.Loading -> {}
        is LoginUiState.Error -> {
            navController.navigate("failure")
        }
        else -> {
            navController.navigate("success")
        }
    }
}