package eu.matoosh.attendance.compose.screen.page.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.matoosh.attendance.R
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.theme.AttendanceTheme

@Composable
fun Confirmation(
    user: User,
    onConfirmed: () -> Unit,
    onCancel: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_overlay))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = user.username,
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = stringResource(id = R.string.text_credit, user.credit),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.size(32.dp))
        Row(
            modifier = Modifier.widthIn(max = dimensionResource(id = R.dimen.max_screen_width))
        ) {
            OutlinedButton(
                onClick = { onCancel() },
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = stringResource(R.string.action_cancel))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    onConfirmed()
                }, // Use the provided onClick event
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = stringResource(id = R.string.action_confirm))
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmationPreview() {
    AttendanceTheme {
        Confirmation(User(1, "matus1", "matvanc@gmail.com", 500), {})
    }
}