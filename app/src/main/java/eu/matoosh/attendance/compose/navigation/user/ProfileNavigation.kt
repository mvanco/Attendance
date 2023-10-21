package eu.matoosh.attendance.compose.navigation.user

import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.matoosh.attendance.compose.navigation.LocalMainNavigationDestination
import eu.matoosh.attendance.compose.screen.page.user.UserProfile
import eu.matoosh.attendance.viewmodels.console.UserProfileViewModel

const val ProfileRoute = "profile"
private const val shouldReloadArg = "should_reload"

fun NavGraphBuilder.profilePage() {
    composable(
        route = "$ProfileRoute?$shouldReloadArg={$shouldReloadArg}",
        arguments = listOf(
            navArgument(shouldReloadArg) {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { dest ->
        val args = ProfileArgs(dest.arguments)
        val mainDest = LocalMainNavigationDestination.current
        val viewModel = hiltViewModel<UserProfileViewModel>(mainDest.owner!!)
        val savedStateHandle = dest.savedStateHandle
        val scanningSuccessful = savedStateHandle.getLiveData<Boolean>(SCANNING_SUCCESSFUL).observeAsState()
        LaunchedEffect(scanningSuccessful.value) {
            if (scanningSuccessful.value == true) {
                viewModel.loadProfile(true)
            }
        }
        UserProfile(
            viewModel = viewModel,
            onFailure = {}
        )
    }
}

internal class ProfileArgs(val shouldReload: Boolean) {
    constructor(bundle: Bundle?) :
            this(
                bundle?.getBoolean(shouldReloadArg) ?: false
            )
}

fun NavController.navigateToProfile(
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(ProfileRoute, builder)
}