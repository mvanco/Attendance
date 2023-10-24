package eu.matoosh.attendance.compose.navigation.user

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.navigation.graph.LocalUserNavigationDestination
import eu.matoosh.attendance.compose.navigation.graph.UserNavigationDestination
import eu.matoosh.attendance.compose.page.user.UserScanner
import eu.matoosh.attendance.viewmodels.console.UserScannerViewModel

const val ScannerRoute = "scanner"
const val SCANNING_SUCCESSFUL = "scanning_successful"

fun NavGraphBuilder.scannerPage(
    onNavigateToProfile: () -> Unit,
    onSetSuccessful: (Boolean) -> Unit
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
    ) { dest ->
        val userScannerViewModel = hiltViewModel<UserScannerViewModel>()
        CompositionLocalProvider(
            LocalUserNavigationDestination provides UserNavigationDestination(dest)
        ) {
            UserScanner(
                userScannerViewModel,
                onSuccess = {
                    onSetSuccessful(true)
                    onNavigateToProfile()
                },
                onError = {
                    onNavigateToProfile()
                }
            )
        }
        LaunchedEffect(route) {
            userScannerViewModel.initialize()
            onSetSuccessful(false)
        }
    }
}