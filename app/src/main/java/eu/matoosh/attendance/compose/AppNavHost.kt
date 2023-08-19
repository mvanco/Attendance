package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.matoosh.attendance.viewmodels.LoginViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            /**
             * Ensures that Login screen will be automatically logout when running again.
             */
            LaunchedEffect(navController) {
                if (navController.currentDestination?.route == "login") {
                    loginViewModel.logout()
                }
            }
            LoginScreen(
                onSuccess = { username ->
                    if (username == "admin") {
                        navController.navigate("mode_selection")
                    }
                    else {
                        navController.navigate("dashboard_screen")
                    }
                },
                loginViewModel = loginViewModel
            )
        }
        composable("mode_selection") {
            ModeSelection {
                when (it) {
                    Mode.ATTENDANCE -> navController.navigate("attendance_sheet")
                    Mode.CONSOLE -> navController.navigate("dashboard_screen?isAdmin=true")
                }
            }
        }
        composable("attendance_sheet") {
            SheetScreen()
        }
        composable(
            "dashboard_screen?isAdmin={isAdmin}",
            arguments = listOf(navArgument("isAdmin") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            if (backStackEntry.arguments?.getBoolean("isAdmin") == true) {
                Message(text = "Dashboard screen of admin")
            }
            else {
                Message(text = "Dashboard screen of user")
            }
        }
    }
}