package eu.matoosh.attendance.compose.widget.global

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun UserForCheckin(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        ElevatedCard(

            modifier = Modifier
                .clickable(onClick = onClick)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize()
            )
        }
    }
}