package eu.matoosh.attendance.compose.widget.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.matoosh.attendance.R
import eu.matoosh.attendance.theme.AttendanceTheme

@Composable
fun LoginForm(
    onClick: (String, String) -> Unit,
    username: MutableState<String> = rememberSaveable {
        mutableStateOf("")
    },
    password: MutableState<String> = rememberSaveable {
        mutableStateOf("")
    },
    showCancelButton: Boolean = false,
    onCancel: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(stringResource(R.string.label_user)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(stringResource(R.string.label_password)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = {
                    onClick(username.value, password.value)
                }
            ),
            modifier = Modifier
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            if (showCancelButton) {
                OutlinedButton(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(stringResource(R.string.action_cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Button(
                onClick = { onClick(username.value, password.value) },
                modifier = Modifier
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
        //LoginForm(onClick = { _, _ -> }, showCancelButton = true)
    }
}