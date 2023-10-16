package eu.matoosh.attendance.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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

    ConstraintLayout {
        val (background, overlay, navHost, navBar) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = stringResource(id = R.string.content_description_background_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.constrainAs(background) {
            }
        )
        Box(
            modifier = Modifier
                .constrainAs(overlay) {
                    top.linkTo(parent.top)
                    bottom.linkTo(navBar.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .background(colorResource(id = R.color.background_overlay))
        )

        UserNavigationBar(
            modifier = Modifier.constrainAs(navBar) {
                width = Dimension.matchParent
                linkTo(parent.start, parent.end)
                bottom.linkTo(parent.bottom)
            },
            selectedRoute = dest.value?.destination?.route ?: ProfileRoute,
            onUserDestinationSelected = { newRoute ->
                val oldRoute = dest.value?.destination?.route
                if (newRoute != oldRoute) {
                    if (oldRoute == ScannerRoute) {
                        userNavController.navigate(newRoute) {
                            popUpTo(oldRoute) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                    else {
                        userNavController.navigate(newRoute) {
                            popUpTo(newRoute) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                }
            }
        )
        UserNavHost(
            userNavController = userNavController,
            modifier = Modifier.constrainAs(navHost) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                linkTo(parent.start, parent.end)
                linkTo(parent.top, navBar.top)
            }
        )
    }
}