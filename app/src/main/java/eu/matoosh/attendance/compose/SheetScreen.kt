package eu.matoosh.attendance.compose

import android.icu.text.UnicodeSetIterator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.viewmodels.BookErrorCode
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreen(
    bookUiState: BookUiState,
    onUserClick: (User) -> Unit,
    onCheckSelectedUser: () -> Unit
) {
    when (bookUiState) {
        is BookUiState.Idle -> {
            SheetForm(
                users = bookUiState.users,
                onUserClick = {
                    onUserClick(it)
                },
            )
        }
        is BookUiState.Confirmation -> {
            Confirmation(
                bookUiState.user,
                onConfirmed = {
                    onCheckSelectedUser()
                }
            )
        }
        is BookUiState.Error -> {
            if (bookUiState.errorCode == BookErrorCode.RENTAL_NOT_FOUND) {
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