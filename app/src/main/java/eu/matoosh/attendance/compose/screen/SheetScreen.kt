package eu.matoosh.attendance.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.widget.global.FullScreenMessage
import eu.matoosh.attendance.compose.page.sheet.Confirmation
import eu.matoosh.attendance.compose.page.sheet.SheetForm
import eu.matoosh.attendance.compose.page.sheet.Success
import eu.matoosh.attendance.compose.widget.global.LoginForm
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.viewmodels.BookErrorCode
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreen(
    bookViewModel: BookViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
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
        },
        onNavigateToLogin = onNavigateToLogin
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
    onCheckSelectedUser: () -> Unit,
    onNavigateToLogin: () -> Unit
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
            when(bookUiState.errorCode) {
                BookErrorCode.RENTAL_NOT_FOUND -> {
                    FullScreenMessage(stringResource(id = R.string.message_sheet_missing))
                }
                BookErrorCode.TOKEN_EXPIRED -> {
                    LaunchedEffect(Unit) {
                        onNavigateToLogin()
                    }
                }
                else -> {
                    FullScreenMessage(stringResource(id = R.string.message_sheet_error))
                }
            }
        }
        BookUiState.Loading -> {
            FullScreenMessage(stringResource(id = R.string.message_sheet_loading))
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