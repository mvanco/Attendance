package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.viewmodels.AppViewModel

@Composable
fun AdminCredits(
    viewModel: AppViewModel = hiltViewModel()
) {
    Message(text = "AdminCredits")
}