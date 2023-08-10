package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.viewmodels.BookViewModel
import eu.matoosh.attendance.viewmodels.LoginViewModel

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