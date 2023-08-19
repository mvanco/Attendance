package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.viewmodels.AppViewModel

@Composable
fun AdminHome(
    viewModel: AppViewModel = hiltViewModel()
) {
    Message(text = "AdminHome", modifier = Modifier.height(1000.dp))
}