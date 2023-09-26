package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lightspark.composeqr.QrCodeView
import eu.matoosh.attendance.R
import eu.matoosh.attendance.viewmodels.console.AdminCreditsUiState
import eu.matoosh.attendance.viewmodels.console.AdminCreditsViewModel

@Composable
fun AdminCredits(
    viewModel: AdminCreditsViewModel = hiltViewModel(),
) {
    val adminCreditsUiState by viewModel.adminCreditsUiState.collectAsState()
    AdminCredits(
        adminCreditsUiState = adminCreditsUiState,
        onQrDone = {
            viewModel.showForm()
        },
        onTopUp = {
            if (it.isNotEmpty()) {
                viewModel.showQr(it.toInt())
            }
        }
    )
}

@Composable
fun AdminCredits(
    adminCreditsUiState: AdminCreditsUiState,
    onQrDone: () -> Unit,
    onTopUp: (String) -> Unit
) {
    when (adminCreditsUiState) {
        is AdminCreditsUiState.Qr -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QrCodeView(
                    data = adminCreditsUiState.authToken,
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = onQrDone) {
                    Text(text = stringResource(id = R.string.action_done, adminCreditsUiState.counter))
                }
            }
        }

        else -> {
            TopUpForm(
                onClick = onTopUp
            )
        }
    }
}


@Composable
fun TopUpForm(
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var credit by rememberSaveable { mutableStateOf<String>("") }

        TextField(
            value = credit,
            onValueChange = { credit = it },
            label = { Text(stringResource(id = R.string.label_credit)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = {
                    onClick(credit)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onClick(credit) },
        ) {
            Text(text = stringResource(id = R.string.action_add_credit))
        }
    }
}