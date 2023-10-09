package eu.matoosh.attendance.compose.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.Mode
import eu.matoosh.attendance.compose.ModeSelectionScreen

const val ModeSelectionRoute = "mode_selection"

fun NavGraphBuilder.modeSelectionScreen(
    onNavigateToAttendanceSheet: () -> Unit,
    onNavigateToAdminConsole: () -> Unit
) {
    composable(ModeSelectionRoute) {
        ModeSelectionScreen ( onSelected = { mode ->
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

fun NavController.navigateToModeSelection(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(ModeSelectionRoute, builder)
}