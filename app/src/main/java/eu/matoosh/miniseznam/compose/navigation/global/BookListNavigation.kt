package eu.matoosh.miniseznam.compose.navigation.global

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.miniseznam.compose.screen.BookListScreen

const val BookListRoute = "book_list"

fun NavGraphBuilder.bookListScreen(
    onNavigateToBookDetail: (String) -> Unit
) {
    composable(BookListRoute) {
        BookListScreen(
            onNavigateToBookDetail = onNavigateToBookDetail
        )
    }
}

fun NavController.navigateToBookList(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(BookListRoute, builder)
}