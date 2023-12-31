package eu.matoosh.attendance.compose.widget.global

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import eu.matoosh.attendance.R
import eu.matoosh.attendance.theme.AttendanceTheme

@Composable
fun LoginForm(
    onClick: (String, String) -> Unit,
    showCancelButton: Boolean = false,
    onCancel: () -> Unit = {}
) {
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    LoginForm(
        onClick,
        username,
        { username = it },
        password,
        { password = it },
        showCancelButton,
        onCancel
    )
}

@Composable
fun LoginForm(
    onClick: (String, String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit = {},
    password: String,
    onPasswordChange: (String) -> Unit = {},
    showCancelButton: Boolean = false,
    onCancel: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_overlay_light))
    ) {
        val (form, title) = createRefs()
        createVerticalChain(title, form, chainStyle = ChainStyle.Packed)
        Image(
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(form.top)
                },
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.content_description_background_image)
        )
        Column(
            modifier = Modifier
                .widthIn(max = dimensionResource(id = R.dimen.max_screen_width))
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .constrainAs(form) {
                    linkTo(parent.start, parent.end)
                    top.linkTo(title.bottom)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            TextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text(stringResource(R.string.label_user)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(R.string.label_password)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onClick(username, password)
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
                    onClick = { onClick(username, password) },
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
            username = "Maty",
            onUsernameChange = {},
            password = "Heslo",
            onPasswordChange = {},
            showCancelButton = true,
            onCancel = {}
        )
    }
}