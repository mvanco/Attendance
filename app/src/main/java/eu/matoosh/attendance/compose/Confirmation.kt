package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.matoosh.attendance.data.User

@Composable
fun Confirmation(
    user: User,
    onConfirmed: () -> Unit
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
        Button(
            onClick = {
                onConfirmed()
            }, // Use the provided onClick event
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Potvrzuji")
        }
    }
}