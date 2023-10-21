package eu.matoosh.attendance.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.matoosh.attendance.compose.navigation.user.ProfileRoute
import eu.matoosh.attendance.compose.navigation.user.SCANNING_SUCCESSFUL
import eu.matoosh.attendance.compose.navigation.user.ScannerRoute
import eu.matoosh.attendance.compose.navigation.user.navigateToProfile
import eu.matoosh.attendance.compose.navigation.user.profilePage
import eu.matoosh.attendance.compose.navigation.user.scannerPage
import eu.matoosh.attendance.compose.navigation.user.termsPage

@Composable
fun UserNavHost(
    userNavController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = userNavController,
        startDestination = ProfileRoute,
        modifier = modifier,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(50, easing = EaseOut)
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(50, delayMillis = 250, easing = EaseIn)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        profilePage()
        termsPage()
        scannerPage(
            onNavigateToProfile = {
                userNavController.popBackStack(ProfileRoute, inclusive = false)
            },
            onSetSuccessful = { successful ->
                val savedStateHandle =
                    userNavController.getBackStackEntry(ProfileRoute).savedStateHandle
                savedStateHandle[SCANNING_SUCCESSFUL] = successful
            }
        )
    }
}