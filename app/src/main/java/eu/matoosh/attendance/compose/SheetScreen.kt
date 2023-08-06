package eu.matoosh.attendance.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    bookViewModel: BookViewModel
) {
    val bookUiState = bookViewModel.bookUiState.observeAsState(BookUiState.Loading)

    when (val state = bookUiState.value) {
        is BookUiState.Idle -> {
            SheetForm(
                bookViewModel = bookViewModel,
                onUserClick = {
                    bookViewModel.selectUser(it.username)
                })
        }
        is BookUiState.Confirmation -> {
            Confirmation(
                state.user,
                onConfirmed = {
                    bookViewModel.checkSelectedUser()
                }
            )
        }
        is BookUiState.Error -> {
            Text("Failure in SheetScreen")
        }
        BookUiState.Init -> {
        }
        BookUiState.Loading -> {
            Message("Načítám...")
        }
        BookUiState.Success -> {
            Success()
        }
    }
}