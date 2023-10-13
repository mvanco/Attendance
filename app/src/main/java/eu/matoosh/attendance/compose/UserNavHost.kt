package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.matoosh.attendance.compose.navigation.user.ProfileRoute
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
        modifier = modifier
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