package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = user.username,
            fontSize = 48.sp,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = stringResource(id = R.string.text_credit, user.credit),
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        Row() {
            OutlinedButton(
                onClick = { onCancel() },
                modifier = Modifier
                    .height(50.dp)
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
                    .height(50.dp)
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