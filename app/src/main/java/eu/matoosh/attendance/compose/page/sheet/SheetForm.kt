package eu.matoosh.attendance.compose.page.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.widget.global.Message
import eu.matoosh.attendance.compose.widget.sheet.UserForCheckin
import eu.matoosh.attendance.data.User

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background_overlay_light))
        ) {
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
}