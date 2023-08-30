package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.matoosh.attendance.R
import eu.matoosh.attendance.theme.AttendanceTheme

@Composable
fun LoginForm(
    onClick: (String, String) -> Unit,
    showCancelButton: Boolean = false,
    onCancel: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.label_user)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            if (showCancelButton) {
                OutlinedButton(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .height(60.dp)
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
            Button(
                onClick = { onClick(username, password) },
                modifier = Modifier
                    .height(60.dp)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(stringResource(R.string.action_login))
            }
        }
    }
}

@Preview
@Composable
private fun LoginFormPreview() {
    AttendanceTheme {
        LoginForm(onClick = { _, _ -> }, showCancelButton = true)
    }
}