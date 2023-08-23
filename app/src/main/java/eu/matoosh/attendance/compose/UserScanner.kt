package eu.matoosh.attendance.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.viewmodels.AppViewModel
import eu.matoosh.attendance.viewmodels.console.UserScannerViewModel

@Composable
fun UserScanner(
    viewModel: UserScannerViewModel = hiltViewModel()
) {
    Scanner(onBarcodeFound = {
        Log.d("barcode", "found barcode: $it")
    })
}