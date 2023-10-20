package eu.matoosh.attendance.compose.navigation.user

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.page.user.UserScanner
import eu.matoosh.attendance.viewmodels.console.UserScannerViewModel

const val ScannerRoute = "scanner"

fun NavGraphBuilder.scannerPage(
    onNavigateToProfile: () -> Unit
) {
    composable(
        ScannerRoute,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        val userScannerViewModel = hiltViewModel<UserScannerViewModel>()
        UserScanner(
            userScannerViewModel,
            onSuccess = {
                onNavigateToProfile()
            }
        )
        LaunchedEffect(route) {
            userScannerViewModel.initialize()
        }
    }
}