package eu.matoosh.attendance.compose.navigation.user

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.navigation.global.LocalMainNavigationDestination
import eu.matoosh.attendance.compose.page.user.UserTerms

const val TermsRoute = "terms"

fun NavGraphBuilder.termsPage() {
    composable(TermsRoute) {
        val mainDest = LocalMainNavigationDestination.current
        UserTerms(userTermsViewModel = hiltViewModel(mainDest.owner!!))
    }
}