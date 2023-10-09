package eu.matoosh.attendance.compose.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.LoginScreen
import eu.matoosh.attendance.viewmodels.LoginViewModel
import androidx.compose.runtime.DisposableEffect
import eu.matoosh.attendance.compose.UserRoute

const val LoginRoute = "login"

fun NavGraphBuilder.loginScreen(
    onNavigateToModeSelection: () -> Unit,
    onNavigateToConsoleScreen: () -> Unit
) {
    composable(LoginRoute) {
        val viewModel: LoginViewModel = hiltViewModel()
        LoginScreen(
            onSuccess = { username ->
                if (username == "admin") {
                    onNavigateToModeSelection()
                } else {
                    onNavigateToConsoleScreen()
                }
            },
            loginViewModel = viewModel
        )
    }
}

fun NavController.consoleNavigateToLogin() {
    navigate(LoginRoute) {
        popBackStack()
    }
}