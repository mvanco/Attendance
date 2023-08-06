package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.matoosh.attendance.viewmodels.BookViewModel
import eu.matoosh.attendance.viewmodels.LoginViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    loginViewModel: LoginViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, loginViewModel = loginViewModel, bookViewModel = bookViewModel)
        }
        composable("attendance_sheet") {
            SheetScreen(navController, bookViewModel = bookViewModel)
        }
    }
}