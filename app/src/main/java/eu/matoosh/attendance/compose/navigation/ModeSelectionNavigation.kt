package eu.matoosh.attendance.compose.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.Mode
import eu.matoosh.attendance.compose.ModeSelection

const val ModeSelectionRoute = "mode_selection"

fun NavGraphBuilder.modeSelectionScreen(
    onNavigateToAttendanceSheet: () -> Unit,
    onNavigateToAdminConsole: () -> Unit
) {
    composable(ModeSelectionRoute) {
        ModeSelection ( onSelected = { mode ->
            when (mode) {
                Mode.ATTENDANCE -> {
                    onNavigateToAttendanceSheet()
                }
                Mode.CONSOLE -> {
                    onNavigateToAdminConsole()
                }
            }
        })
    }
}

fun NavController.navigateLoginToModeSelection() {
    navigate(ModeSelectionRoute)
}