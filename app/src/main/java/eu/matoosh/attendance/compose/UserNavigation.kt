package eu.matoosh.attendance.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import eu.matoosh.attendance.R

object UserRoute {
    const val PROFILE = "Profile"
    const val TERMS = "Terms"
    const val SCANNER = "Scanner"
}

data class UserDestination(
    val route: String,
    val icon: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null,
    @StringRes val titleId: Int
)

val USER_DESTINATIONS = listOf(
    UserDestination(
        route = UserRoute.PROFILE,
        icon = Icons.Default.Person,
        titleId = R.string.label_profile
    ),
    UserDestination(
        route = UserRoute.TERMS,
        icon = Icons.Default.List,
        titleId = R.string.label_terms
    ),
    UserDestination(
        route = UserRoute.SCANNER,
        icon = Icons.Default.AddCircle,
        titleId = R.string.label_scanner
    )
)