package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.compose.navigation.LoginRoute
import eu.matoosh.attendance.compose.navigation.attendanceSheetScreen
import eu.matoosh.attendance.compose.navigation.consoleNavigateToLogin
import eu.matoosh.attendance.compose.navigation.consoleScreen
import eu.matoosh.attendance.compose.navigation.loginScreen
import eu.matoosh.attendance.compose.navigation.modeSelectionScreen
import eu.matoosh.attendance.compose.navigation.navigateLoginToConsole
import eu.matoosh.attendance.compose.navigation.navigateModeSelectionToConsole
import eu.matoosh.attendance.compose.navigation.navigateModeSelectionToAttendanceSheet
import eu.matoosh.attendance.compose.navigation.navigateLoginToModeSelection

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {
        loginScreen(
            onNavigateToConsoleScreen = {
                navController.navigateLoginToConsole()
            },
            onNavigateToModeSelection = {
                navController.navigateLoginToModeSelection()
            }
        )
        modeSelectionScreen(
            onNavigateToAttendanceSheet = {
                navController.navigateModeSelectionToAttendanceSheet()
            },
            onNavigateToAdminConsole = {
                navController.navigateModeSelectionToConsole(true)
            }
        )
        attendanceSheetScreen()
        consoleScreen(
            onNavigateToLogin = {
                navController.consoleNavigateToLogin()
            }
        )
    }
}