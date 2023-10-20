package eu.matoosh.attendance.compose.widget.user

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import eu.matoosh.attendance.compose.screen.USER_DESTINATIONS

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

@Composable
fun UserNavigationBar(
    selectedRoute: String,
    onUserDestinationSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        USER_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedRoute.takeWhile { it != '?' } == destination.route,
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