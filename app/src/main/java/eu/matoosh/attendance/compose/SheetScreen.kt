package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import eu.matoosh.attendance.viewmodels.BookErrorCode
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreen(
    bookViewModel: BookViewModel
) {
    val bookUiState = bookViewModel.bookUiState.collectAsState()

    when (val state = bookUiState.value) {
        is BookUiState.Idle -> {
            SheetForm(
                bookViewModel = bookViewModel,
                onUserClick = {
                    bookViewModel.selectUser(it.username)
                },
                bookUiState = bookUiState.value)
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
            if (state.errorCode == BookErrorCode.RENTAL_NOT_FOUND) {
                Message("Momentálně neprobíhá žádné bruslení.")
            }
            else {
                Message("Nastala chyba při načítání uživatelů. :(")
            }
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