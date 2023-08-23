package eu.matoosh.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserForCheckin(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            Text(
                text = text,
                fontSize = 22.sp,
                style = MaterialTheme.typography.body1,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
    }
}