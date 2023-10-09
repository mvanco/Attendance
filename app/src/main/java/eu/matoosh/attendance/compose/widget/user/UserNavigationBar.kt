package eu.matoosh.attendance.compose.widget.user

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.navigation.user.ProfileRoute
import eu.matoosh.attendance.compose.navigation.user.ScannerRoute
import eu.matoosh.attendance.compose.navigation.user.TermsRoute

sealed interface Icon {
    data class IconImageVector(val vector: ImageVector?) : Icon
    data class IconVectorAsset(@DrawableRes val iconRes: Int) : Icon
}

data class UserDestination(
    val route: String,
    val icon: Icon? = null,
    @DrawableRes val iconRes: Int? = null,
    @StringRes val titleId: Int
)

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

@Composable
fun UserNavigationBar(
    selectedRoute: String,
    onUserDestinationSelected: (String) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        USER_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedRoute == destination.route,
                onClick = {
                    onUserDestinationSelected(destination.route)
                },
                icon = {
                    when (destination.icon) {
                        is Icon.IconImageVector -> {
                            Icon(
                                imageVector = destination.icon.vector!!,
                                contentDescription = stringResource(id = destination.titleId)
                            )
                        }
                        is Icon.IconVectorAsset -> {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = destination.icon.iconRes),
                                contentDescription = stringResource(id = destination.titleId)
                            )
                        }
                        else -> {}
                    }

                },
                label = {
                    Text(text = stringResource(id = destination.titleId))
                }
            )
        }
    }
}