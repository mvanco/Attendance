package eu.matoosh.attendance.seznam.navigation.global

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
const val BookDetailRoute = "book_detail"
private const val BOOK_ID = "book_id"

fun NavGraphBuilder.bookDetailScreen() {
    composable("$BookDetailRoute/{$BOOK_ID}") {

    }
}

fun NavController.navigateToBookDetail(id: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate("$BookDetailRoute/$id", builder)
}