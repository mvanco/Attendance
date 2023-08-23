package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.viewmodels.console.UserScannerUiState
import eu.matoosh.attendance.viewmodels.console.UserScannerViewModel

@Composable
fun UserScanner(
    viewModel: UserScannerViewModel = hiltViewModel()
) {
    val userScannerUiState by viewModel.userScannerUiState.collectAsState()

    UserScanner(
        userScannerUiState = userScannerUiState,
        onBarcodeFound = { credit ->
            viewModel.addCredit(credit)
        }
    )
}

@Composable
fun UserScanner(
    userScannerUiState: UserScannerUiState,
    onBarcodeFound: (String) -> Unit
) {
    when (userScannerUiState) {
        is UserScannerUiState.Scanner -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Scanner(
                    onBarcodeFound = onBarcodeFound
                )
            }
        }
        is UserScannerUiState.Success -> {
            Message(text = "Kredit byl úspěšně přidán.")
        }
    }
}