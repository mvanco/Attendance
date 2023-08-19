package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.viewmodels.AppViewModel

@Composable
fun UserScanner(
    viewModel: AppViewModel = hiltViewModel()
) {
    Message(text = "UserScanner")
}