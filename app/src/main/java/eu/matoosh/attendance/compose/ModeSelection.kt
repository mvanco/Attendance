package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.matoosh.attendance.ui.theme.AttendanceTheme

enum class Mode {
    ATTENDANCE,
    CONSOLE
}

@Composable
fun ModeSelection(
    onSelected: (Mode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onSelected(Mode.ATTENDANCE) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Docházková kniha")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onSelected(Mode.CONSOLE)
            }, // Use the provided onClick event
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Konzola")
        }
    }
}

@Preview
@Composable
private fun ModeSelectionPreview() {
    AttendanceTheme {
        ModeSelection { mode ->

        }
    }
}