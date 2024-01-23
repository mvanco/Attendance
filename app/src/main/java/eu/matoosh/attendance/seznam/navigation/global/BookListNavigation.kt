package eu.matoosh.attendance.seznam.navigation.global

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.seznam.navigation.screen.BookListScreen

const val BookListRoute = "book_list"

fun NavGraphBuilder.bookListScreen(
    onNavigateToBookDetail: (String) -> Unit
) {
    composable(BookListRoute) {
        BookListScreen(onNavigateToBookDetail = {})
    }
}

fun NavController.navigateToBookList(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(BookListRoute, builder)
}