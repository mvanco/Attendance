package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreenWrapper(
                onSuccess = {
                    navController.navigate("attendance_sheet")
                }
            )
        }
        composable("attendance_sheet") {
            SheetScreenWrapper()
        }
    }
}