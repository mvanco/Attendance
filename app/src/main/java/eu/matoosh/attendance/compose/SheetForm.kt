package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import eu.matoosh.attendance.R
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetForm(
    modifier: Modifier = Modifier,
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    if (users.isEmpty()) {
        Message(text = stringResource(id = R.string.message_sheet_success))
    }
    else {
        val addManually = User(-1, stringResource(id = R.string.action_add_manually), "", 0)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.testTag("attendance_sheet"),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.card_side_margin),
                vertical = dimensionResource(id = R.dimen.header_margin)
            )
        ) {
            items(
                items = users + addManually,
                key = { it.id }
            ) { user ->
                UserForCheckin(
                    text = user.username,
                    onClick = { onUserClick(user) }
                )
            }
        }
    }
}