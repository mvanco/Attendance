package eu.matoosh.attendance.compose.navigation.global

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.Mode
import eu.matoosh.attendance.compose.screen.ModeSelectionScreen

const val ModeSelectionRoute = "mode_selection"

fun NavGraphBuilder.selectionScreen(
    onNavigateToAttendanceSheet: () -> Unit,
    onNavigateToAdminConsole: () -> Unit
) {
    composable(ModeSelectionRoute) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
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
}

fun NavController.navigateToSelection(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(ModeSelectionRoute, builder)
}