package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.LoginViewModel

@Composable
fun LoginScreenWrapper(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreen(
        navController,
        loginViewModel
    )
}