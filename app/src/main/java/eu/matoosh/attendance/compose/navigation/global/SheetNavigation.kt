package eu.matoosh.attendance.compose.navigation.global

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.SheetScreen

const val SheetRoute = "attendance_sheet"

fun NavGraphBuilder.sheetScreen() {
    composable(SheetRoute) {
        SheetScreen()
    }
}

fun NavController.navigateToSheet(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(SheetRoute, builder)
}