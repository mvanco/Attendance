package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.ui.theme.AttendanceTheme

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
            text = "Aktuální zůstatek: ${user.credit}Kč",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        Row() {
            Button(
                onClick = { onCancel() },
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            ) {
                Text(text = "Zrušit")
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
                Text(text = "Potvrzuji")
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