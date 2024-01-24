package eu.matoosh.attendance.compose.navigation.global

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import eu.matoosh.attendance.compose.screen.BookDetailScreen

const val BookDetailRoute = "book_detail"
private const val BOOK_ID = "book_id"

fun NavGraphBuilder.bookDetailScreen() {
    composable("$BookDetailRoute/{$BOOK_ID}") { dest ->
        val args = BookDetailArgs(dest.arguments)
        BookDetailScreen(
            id = args.bookId
        )
    }
}

fun NavController.navigateToBookDetail(id: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate("$BookDetailRoute/$id", builder)
}

internal class BookDetailArgs(val bookId: String) {
    constructor(bundle: Bundle?) :
            this(
                bookId = bundle?.getString(BOOK_ID) ?: ""
            )
}