package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var shouldLogout by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onSuccess = {
                    navController.navigate("attendance_sheet")
                },
                shouldLogout = shouldLogout,
                onShouldLogoutChange = {
                    shouldLogout = it
                },
                currentRoute = navController.currentDestination?.route
            )
        }
        composable("attendance_sheet") {
            SheetScreen()
            shouldLogout = true
        }
    }
}