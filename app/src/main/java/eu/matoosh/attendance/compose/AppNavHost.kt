package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onSuccess = {
                    navController.navigate("attendance_sheet")
                },
                loginViewModel = loginViewModel
            )
        }
        composable("attendance_sheet") {
            SheetScreen()
        }
    }
}