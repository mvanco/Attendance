package eu.matoosh.attendance.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import eu.matoosh.attendance.R

object UserRoute {
    const val PROFILE = "Profile"
    const val TERMS = "Terms"
    const val SCANNER = "Scanner"
}

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
        route = UserRoute.PROFILE,
        icon = Icon.IconImageVector(Icons.Default.Person),
        titleId = R.string.label_profile
    ),
    UserDestination(
        route = UserRoute.TERMS,
        icon = Icon.IconImageVector(Icons.Default.List),
        titleId = R.string.label_terms
    ),
    UserDestination(
        route = UserRoute.SCANNER,
        icon = Icon.IconVectorAsset(R.drawable.ic_photo),
        titleId = R.string.label_scanner
    )
)