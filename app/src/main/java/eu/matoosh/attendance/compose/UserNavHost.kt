package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.matoosh.attendance.compose.navigation.user.ProfileRoute
import eu.matoosh.attendance.compose.navigation.user.profilePage
import eu.matoosh.attendance.compose.navigation.user.scannerPage
import eu.matoosh.attendance.compose.navigation.user.termsPage

@Composable
fun UserNavHost(
    userNavController: NavHostController
) {
    NavHost(
        navController = userNavController,
        startDestination = ProfileRoute
    ) {
        profilePage()
        termsPage()
        scannerPage(
            onNavigateToProfile = {
                userNavController.popBackStack()
            }
        )
    }
}