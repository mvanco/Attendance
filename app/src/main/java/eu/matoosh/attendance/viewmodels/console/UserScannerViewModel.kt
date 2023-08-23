package eu.matoosh.attendance.viewmodels.console

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Stable
sealed interface UserScannerUiState {
    object Scanner : UserScannerUiState
    object Success : UserScannerUiState
}

@HiltViewModel
class UserScannerViewModel @Inject constructor(

) : ViewModel() {

}