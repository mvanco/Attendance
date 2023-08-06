package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.viewmodels.BookUiState
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetForm(
    modifier: Modifier = Modifier,
    bookViewModel: BookViewModel = hiltViewModel(),
    onUserClick: (User) -> Unit
) {
    val users = (bookViewModel.bookUiState.value as? BookUiState.Idle)?.users ?: emptyList()

    if (users.isEmpty()) {
        Message(text = "Všichni účastníci byli odbaveni. :)")
    }
    else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.testTag("attendance_sheet"),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.card_side_margin),
                vertical = dimensionResource(id = R.dimen.header_margin)
            )
        ) {
            items(
                items = users,
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