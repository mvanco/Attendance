package eu.matoosh.attendance.compose.screen.page.user

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.screen.page.Message
import eu.matoosh.attendance.compose.widget.user.Scanner
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.console.UserScannerUiState
import eu.matoosh.attendance.viewmodels.console.UserScannerViewModel
import kotlinx.coroutines.delay

@Composable
fun UserScanner(
    viewModel: UserScannerViewModel = hiltViewModel(),
    onSuccess: () -> Unit
) {
    val userScannerUiState by viewModel.userScannerUiState.collectAsState()

    UserScanner(
        userScannerUiState = userScannerUiState,
        onBarcodeFound = { credit ->
            viewModel.addCredit(credit)
        },
        onSuccess = onSuccess
    )
}

@Composable
fun UserScanner(
    userScannerUiState: UserScannerUiState,
    onBarcodeFound: (String) -> Unit,
    onSuccess: () -> Unit
) {
    when (userScannerUiState) {
        is UserScannerUiState.Scanner -> {
            val context = LocalContext.current
            val cameraPermissionState = remember { mutableStateOf(checkCameraPermission(context)) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    cameraPermissionState.value = permissions[Manifest.permission.CAMERA] == true
                }
            )
            if (cameraPermissionState.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Scanner(
                        onBarcodeFound = onBarcodeFound
                    )
                }
            }
            else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA)) }) {
                        Text(stringResource(R.string.action_add_camera_permission))
                    }
                }
            }

        }
        is UserScannerUiState.Success -> {
            Message(text = stringResource(id = R.string.message_user_added_credit))
            LaunchedEffect(Unit) {
                delay(LoginUiState.SUCCESS_STATE_DURATION)
                onSuccess()
            }
        }
    }
}

fun checkCameraPermission(context: Context): Boolean {
    val cameraPermission = Manifest.permission.CAMERA
    return ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
}





