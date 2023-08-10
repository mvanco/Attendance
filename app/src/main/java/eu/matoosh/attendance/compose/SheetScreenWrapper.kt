package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreenWrapper(
    bookViewModel: BookViewModel = hiltViewModel()
) {
    val bookUiState = bookViewModel.bookUiState.collectAsState()
    SheetScreen(
        bookUiState.value,
        onUserClick = {
                      bookViewModel.selectUser(it.username)
        },
        onCheckSelectedUser = {
            bookViewModel.checkSelectedUser()
        }
    )
}