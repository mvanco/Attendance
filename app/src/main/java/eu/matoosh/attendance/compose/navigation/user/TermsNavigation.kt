package eu.matoosh.attendance.compose.navigation.user

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.page.user.UserTerms

const val TermsRoute = "terms"

fun NavGraphBuilder.termsPage() {
    composable(TermsRoute) {
        UserTerms()
    }
}