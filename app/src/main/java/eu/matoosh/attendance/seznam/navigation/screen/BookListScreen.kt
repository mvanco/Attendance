package eu.matoosh.attendance.seznam.navigation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.seznam.viewmodel.BookListUiState
import eu.matoosh.attendance.seznam.viewmodel.BookListViewModel

@Composable
fun BookListScreen(
    bookViewModel: BookListViewModel = hiltViewModel(),
    onNavigateToBookDetail: (String) -> Unit
) {
    val bookUiState: BookListUiState by bookViewModel.bookUiState.collectAsState()
    when (bookUiState) {
        is BookListUiState.Idle -> {
            Text(text = (bookUiState as BookListUiState.Idle).books[0].description ?: "unknown")
        }
        else -> {
            Text(text = "Loading...")
        }
    }
}