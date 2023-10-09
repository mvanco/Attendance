package eu.matoosh.attendance.compose.navigation

import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.matoosh.attendance.compose.ConsoleScreen

const val ConsoleRoute = "console"
private const val isAdminArg = "isAdmin"

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.consoleScreen(
    onNavigateAdminToLogin: () -> Unit,
    onNavigateUserToLogin: () -> Unit
) {
    composable(
        "$ConsoleRoute?$isAdminArg={$isAdminArg}",
        arguments = listOf(
            navArgument(isAdminArg) {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { dest ->
        val args = ConsoleArgs(dest.arguments)
        ConsoleScreen(
            isAdmin = args.isAdmin,
            onNavigateAdminToLogin = onNavigateAdminToLogin,
            onNavigateUserToLogin = onNavigateUserToLogin
        )
    }
}

fun NavController.navigateToConsole(isAdmin: Boolean = false, builder: NavOptionsBuilder.() -> Unit = {}) {
    val arg = if (isAdmin) "true" else "false"
    this.navigate("$ConsoleRoute?$isAdminArg=$arg", builder)
}

internal class ConsoleArgs(val isAdmin: Boolean) {
    constructor(bundle: Bundle?) :
            this(
                bundle?.getBoolean(isAdminArg) ?: false
            )
}