package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.compose.navigation.LoginRoute
import eu.matoosh.attendance.compose.navigation.ModeSelectionRoute
import eu.matoosh.attendance.compose.navigation.attendanceSheetScreen
import eu.matoosh.attendance.compose.navigation.consoleScreen
import eu.matoosh.attendance.compose.navigation.loginScreen
import eu.matoosh.attendance.compose.navigation.modeSelectionScreen
import eu.matoosh.attendance.compose.navigation.navigateToAttendanceSheet
import eu.matoosh.attendance.compose.navigation.navigateToConsole
import eu.matoosh.attendance.compose.navigation.navigateToLogin
import eu.matoosh.attendance.compose.navigation.navigateToModeSelection

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {
        loginScreen(
            onNavigateToConsoleScreen = {
                navController.navigateToConsole {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            },
            onNavigateToModeSelection = {
                navController.popBackStack()
                navController.popBackStack()
                navController.navigateToLogin()
                navController.navigateToModeSelection()
            }
        )
        modeSelectionScreen(
            onNavigateToAttendanceSheet = {
                navController.navigateToAttendanceSheet() {
                    popUpTo(ModeSelectionRoute) {
                        inclusive = true
                    }
                }
            },
            onNavigateToAdminConsole = {
                navController.navigateToConsole(true)
            }
        )
        attendanceSheetScreen()
        consoleScreen(
            onNavigateAdminToLogin = {
                navController.navigateToLogin {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            },
            onNavigateUserToLogin = {
                navController.popBackStack()
                navController.navigateToLogin()
            }
        )
    }
}