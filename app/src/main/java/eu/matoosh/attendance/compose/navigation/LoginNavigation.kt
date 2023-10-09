package eu.matoosh.attendance.compose.navigation

import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.matoosh.attendance.compose.screen.LoginScreen
import eu.matoosh.attendance.viewmodels.LoginViewModel

const val LoginRoute = "login"
private const val usernameArg = "username"

fun NavGraphBuilder.loginScreen(
    onNavigateToModeSelection: () -> Unit,
    onNavigateToConsoleScreen: () -> Unit
) {
    composable(
        LoginRoute
    ) {
        val viewModel: LoginViewModel = hiltViewModel()
        LoginScreen(
            onSuccess = { username ->
                if (username == "admin") {
                    onNavigateToModeSelection()
                } else {
                    onNavigateToConsoleScreen()
                }
            },
            onFailure = {
                viewModel.initialize("", "")
            },
            loginViewModel = viewModel
        )
    }

    composable(
        "login?username={username}&password={password}",
        listOf(
            navArgument("username") {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument("password") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { dest ->
        val args = LoginArgs(dest.arguments)
        val viewModel: LoginViewModel = hiltViewModel()
        LaunchedEffect(key1 = viewModel) {
            viewModel.initialize(args.username, "")
        }
        LoginScreen(
            onSuccess = { username ->
                if (username == "admin") {
                    onNavigateToModeSelection()
                } else {
                    onNavigateToConsoleScreen()
                }
            },
            onFailure = {
                viewModel.initialize("", "")
            },
            loginViewModel = viewModel
        )
    }
}

fun NavController.navigateToLogin(
    username: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate("$LoginRoute?$usernameArg=$username")
}

fun NavController.navigateToLogin(
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(LoginRoute, builder)
}

internal class LoginArgs(val username: String) {
    constructor(bundle: Bundle?) :
            this(
                bundle?.getString(usernameArg) ?: ""
            )
}