package eu.matoosh.attendance.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.matoosh.attendance.R
import eu.matoosh.attendance.theme.AttendanceTheme

enum class Mode {
    ATTENDANCE,
    CONSOLE
}

@Composable
fun ModeSelectionScreen(
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
            Text(
                text = stringResource(R.string.action_sheet),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                onSelected(Mode.CONSOLE)
            }, // Use the provided onClick event
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.action_console),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview
@Composable
private fun ModeSelectionPreview() {
    AttendanceTheme {
        ModeSelectionScreen { mode ->

        }
    }
}