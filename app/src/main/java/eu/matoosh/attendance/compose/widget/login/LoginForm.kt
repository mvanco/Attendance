package eu.matoosh.attendance.compose.widget.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
    onCancel: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (form, title) = createRefs()
        Row(
            modifier = Modifier
                .constrainAs(title) {
                    linkTo(parent.top, form.top, bias = 0.4f)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skater),
                contentDescription = stringResource(id = R.string.content_description_background_image),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        }
        Column(
            modifier = Modifier
                .widthIn(max = dimensionResource(id = R.dimen.max_screen_width))
                .padding(16.dp)
                .constrainAs(form) {
                    linkTo(parent.top, parent.bottom, bias = 0.6f)
                    linkTo(parent.start, parent.end)
                }
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
}

@Preview
@Composable
private fun LoginFormPreview() {
    AttendanceTheme {
        LoginForm(
            onClick = { _, _ -> },
            username = rememberSaveable { mutableStateOf("Maty") },
            password = rememberSaveable { mutableStateOf("Heslo") },
            showCancelButton = true,
            onCancel = {}
        )
    }
}