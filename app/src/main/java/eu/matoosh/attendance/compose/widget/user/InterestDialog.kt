package eu.matoosh.attendance.compose.widget.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eu.matoosh.attendance.R
import eu.matoosh.attendance.data.Interest
import eu.matoosh.attendance.theme.AttendanceTheme
import java.time.ZonedDateTime
import androidx.compose.foundation.lazy.items
import eu.matoosh.attendance.utils.frontendFormatter
import eu.matoosh.attendance.utils.toFormattedString

@Composable
fun InterestDialog(
    interestsToReg: List<Interest>,
    onTermSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedTerm by rememberSaveable { mutableStateOf<Int?>(null) }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column() {
                Text(
                    text = stringResource(id = R.string.label_register),
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 24.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium
                )
                Divider(
                    color = MaterialTheme.colorScheme.outline
                )
                val scrollState = rememberScrollState()
                LazyColumn(
                    modifier = Modifier.verticalScroll(scrollState).heightIn(max = 250.dp)
                ) {
                    items(interestsToReg) { interest ->
                        InterestRow(
                            selected = interest.rentalId == selectedTerm,
                            onClick = {
                                selectedTerm = interest.rentalId
                            },
                            text = interest.start.toFormattedString(frontendFormatter),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Divider(
                    color = MaterialTheme.colorScheme.outline
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.action_cancel))
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    OutlinedButton(
                        onClick = { selectedTerm?.let { onTermSelect(it) } },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.action_confirm))
                    }
                }
            }
        }
    }
}

@Composable
fun InterestRow(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = Modifier.padding(0.dp)
        )
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
private fun InterestDialogPreview() {
    AttendanceTheme {
        var interestsToReg = mutableListOf<Interest>()
        repeat(6) {
            interestsToReg.add(Interest(60, 500, 1, ZonedDateTime.now(), false))
        }
        InterestDialog(
            interestsToReg = interestsToReg,
            onTermSelect = {},
            onDismissRequest = {},
        )
    }
}