package eu.matoosh.attendance.compose.screen.page.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.AppBarAction
import eu.matoosh.attendance.compose.AppBarActions
import eu.matoosh.attendance.compose.FabState
import eu.matoosh.attendance.compose.LocalOnAppBarActionsChange
import eu.matoosh.attendance.compose.LocalOnFabStateChange
import eu.matoosh.attendance.compose.screen.page.Message
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.console.ProfileErrorCode
import eu.matoosh.attendance.viewmodels.console.UserProfileUiState
import eu.matoosh.attendance.viewmodels.console.UserProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun UserProfile(
    viewModel: UserProfileViewModel = hiltViewModel(),
    onFailure: () -> Unit
) {
    val userProfilUiState by viewModel.userProfileUiState.collectAsState()

    LocalOnFabStateChange.current.onChange(
        FabState(false)
    )

    LocalOnAppBarActionsChange.current.onChange(
        AppBarActions(listOf(
            AppBarAction(
                onClickListener = {},
                icon = ImageVector.vectorResource(R.drawable.ic_world),
                iconDescription = ""
            )
        ))
    )

    UserProfile(
        uiState = userProfilUiState,
        onFailure  = onFailure
    )
}

@Composable
fun UserProfile(
    uiState: UserProfileUiState,
    onFailure: () -> Unit
) {
    when (uiState) {
        is UserProfileUiState.Error -> {
            when (uiState.errorCode) {
                ProfileErrorCode.UNKNOWN_ERROR -> {
                    Message(stringResource(R.string.message_user_profile_unknown))
                }
            }
            LaunchedEffect(Unit) {
                delay(LoginUiState.FAILURE_STATE_DURATION)
                onFailure()
            }
        }

        is UserProfileUiState.Idle -> {
            val user = uiState.user
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    text = stringResource(id = R.string.text_credit, user.credit),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        is UserProfileUiState.Loading -> {
            Message(stringResource(id = R.string.message_user_loading))
        }
    }
}