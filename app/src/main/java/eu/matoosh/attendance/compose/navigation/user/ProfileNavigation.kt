package eu.matoosh.attendance.compose.navigation.user

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.navigation.LocalMainNavigationDestination
import eu.matoosh.attendance.compose.screen.page.user.UserProfile

const val ProfileRoute = "profile"

fun NavGraphBuilder.profilePage() {
    composable(
        route = ProfileRoute
    ) {
        val mainDest = LocalMainNavigationDestination.current
        UserProfile(
            viewModel = hiltViewModel(mainDest.owner!!),
            onFailure = {}
        )
    }
}