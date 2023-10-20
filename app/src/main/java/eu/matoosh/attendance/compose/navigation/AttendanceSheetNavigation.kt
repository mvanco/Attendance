package eu.matoosh.attendance.compose.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.SheetScreen

const val AttendanceSheetRoute = "attendance_sheet"

fun NavGraphBuilder.attendanceSheetScreen() {
    composable(AttendanceSheetRoute) {
        SheetScreen()
    }
}

fun NavController.navigateToAttendanceSheet(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(AttendanceSheetRoute, builder)
}