package eu.matoosh.attendance.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.UserNavHost
import eu.matoosh.attendance.compose.navigation.user.ProfileRoute
import eu.matoosh.attendance.compose.navigation.user.ScannerRoute
import eu.matoosh.attendance.compose.navigation.user.TermsRoute
import eu.matoosh.attendance.compose.widget.user.Icon
import eu.matoosh.attendance.compose.widget.user.UserDestination
import eu.matoosh.attendance.compose.widget.user.UserNavigationBar

val USER_DESTINATIONS = listOf(
    UserDestination(
        route = ProfileRoute,
        icon = Icon.IconImageVector(Icons.Default.Person),
        titleId = R.string.label_profile
    ),
    UserDestination(
        route = TermsRoute,
        icon = Icon.IconImageVector(Icons.Default.List),
        titleId = R.string.label_terms
    ),
    UserDestination(
        route = ScannerRoute,
        icon = Icon.IconVectorAsset(R.drawable.ic_photo),
        titleId = R.string.label_scanner
    )
)

fun List<UserDestination>.findDest(route: String?): UserDestination? {
    return this.find { it.route == route}
}

@Composable
fun UserConsoleScreen(
    userNavController: NavHostController = rememberNavController()
) {
    val dest = userNavController.currentBackStackEntryAsState()

    Column(
        modifier = Modifier.background(colorResource(id = R.color.background_overlay))
    ) {
        UserNavHost(
            userNavController = userNavController,
            modifier = Modifier.weight(1f)
        )
        UserNavigationBar(
            selectedRoute = dest.value?.destination?.route ?: ProfileRoute,
            onUserDestinationSelected = { route ->
                userNavController.navigate(route) {
                    popUpTo(route) {
                        this.inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}