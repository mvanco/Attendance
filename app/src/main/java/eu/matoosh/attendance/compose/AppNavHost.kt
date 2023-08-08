package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreenWrapper(navController)
        }
        composable("attendance_sheet") {
            SheetScreenWrapper(navController)
        }
    }
}