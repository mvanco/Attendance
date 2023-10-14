package eu.matoosh.attendance.compose.navigation.user

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.page.user.UserProfile
import eu.matoosh.attendance.viewmodels.console.UserProfileViewModel

const val ProfileRoute = "profile"

fun NavGraphBuilder.profilePage() {
    composable(ProfileRoute) {
        val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
        UserProfile(
            userProfileViewModel,
            onFailure = {}
        )
        LaunchedEffect(route) {
            userProfileViewModel.loadProfile()
        }
    }
}