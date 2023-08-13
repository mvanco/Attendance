package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.viewmodels.BookErrorCode
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreen(
    bookViewModel: BookViewModel = hiltViewModel()
) {
    val bookUiState by bookViewModel.bookUiState.collectAsState()
    SheetScreen(
        bookUiState = bookUiState,
        onUserClick = {
            bookViewModel.selectUser(it.username)
        },
        onCheckSelectedUser = {
            bookViewModel.checkSelectedUser()
        },
        onCheckUnknownUser = {
            bookViewModel.checkUnknownUser()
        },
        onAddManuallyClick = { username, password ->
            bookViewModel.checkUserManually(username, password)
        },
        onManualCancelled = {
            bookViewModel.cancel()
        },
        onConfirmationCancelled = {
            bookViewModel.cancel()
        }
    )
}

@Composable
fun SheetScreen(
    bookUiState: BookUiState,
    onUserClick: (User) -> Unit,
    onAddManuallyClick: (String, String) -> Unit,
    onCheckUnknownUser: () -> Unit,
    onManualCancelled: () -> Unit,
    onConfirmationCancelled: () -> Unit,
    onCheckSelectedUser: () -> Unit
) {
    when (bookUiState) {
        is BookUiState.Idle -> {
            SheetForm(
                users = bookUiState.users,
                onUserClick = {
                    if (it.id == -1) {
                        onCheckUnknownUser()
                    }
                    else {
                        onUserClick(it)
                    }
                },
            )
        }
        is BookUiState.Confirmation -> {
            Confirmation(
                bookUiState.user,
                onConfirmed = {
                    onCheckSelectedUser()
                },
                onCancel = {
                    onConfirmationCancelled()
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

        BookUiState.Manual -> {
            LoginForm(
                onClick = { username, password ->
                    onAddManuallyClick(username, password)
                },
                showCancelButton = true,
                onCancel = {
                    onManualCancelled()
                }
            )
        }
    }
}