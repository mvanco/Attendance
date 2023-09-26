package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.data.Interest
import eu.matoosh.attendance.theme.AttendanceTheme
import eu.matoosh.attendance.utils.frontendFormatter
import eu.matoosh.attendance.utils.toFormattedString
import eu.matoosh.attendance.viewmodels.LoginUiState
import eu.matoosh.attendance.viewmodels.console.UserTermsErrorCode
import eu.matoosh.attendance.viewmodels.console.UserTermsUiState
import eu.matoosh.attendance.viewmodels.console.UserTermsViewModel
import kotlinx.coroutines.delay
import java.time.ZonedDateTime

@Composable
fun UserTerms(
    userTermsViewModel: UserTermsViewModel = hiltViewModel()
) {
    val userTermsUiState by userTermsViewModel.uiState.collectAsState()
    UserTerms(
        userTermsUiState = userTermsUiState,
        onRegTermClick = {
            userTermsViewModel.selectTerm()
        },
        onTermSelect = {
            userTermsViewModel.register(it)
        },
        onDismissRequest = {
            userTermsViewModel.dismiss()
        },
        onError = {
            userTermsViewModel.dismiss()
        }
    )
}

@Composable
fun UserTerms(
    userTermsUiState: UserTermsUiState,
    onRegTermClick: () -> Unit,
    onTermSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onError: () -> Unit
) {
    when (userTermsUiState) {
        is UserTermsUiState.Idle -> {
            UserTerms(
                userTermsUiState.interests,
                userTermsUiState.registrationEnabled,
                onRegTermClick,
                onTermSelect
            )
        }
        is UserTermsUiState.NewInterest -> {
            UserTerms(
                userTermsUiState.interests,
                true,
                onRegTermClick,
                onTermSelect
            )
        }
        is UserTermsUiState.Error -> {
            when (userTermsUiState.errorCode) {
                UserTermsErrorCode.INSUFFICIENT_CREDIT -> {
                    Message(stringResource(id = R.string.message_user_error_insufficient_credit))
                    LaunchedEffect(Unit) {
                        delay(LoginUiState.FAILURE_STATE_DURATION)
                        onError()
                    }
                }
                UserTermsErrorCode.UNKNOWN_ERROR, UserTermsErrorCode.INVALID_TOKEN  -> {
                    Message(stringResource(id = R.string.message_user_error))
                }
            }
        }
        is UserTermsUiState.Loading -> {
            Message(stringResource(id = R.string.message_user_loading))
        }
        else -> {}
    }
    if (userTermsUiState is UserTermsUiState.NewInterest) {
        InterestDialog(userTermsUiState.interestsToReg, onTermSelect, onDismissRequest)
    }
}

@Composable
fun UserTerms(
    interests: List<Interest>,
    registrationEnabled: Boolean,
    onRegTermClick: () -> Unit,
    onTermSelect: (Int) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            if (registrationEnabled) {
                ExtendedFloatingActionButton(
                    onClick = { onRegTermClick() },
                    icon = {
                        Icon(
                            Icons.Filled.Create,
                            stringResource(id = R.string.content_description_register_term_fab)
                        )
                    },
                    text = {
                        Text(text = stringResource(id = R.string.action_register_term))
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (interests.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.label_my_terms),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = stringResource(id = R.string.message_user_no_interest),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            else {
                Text(
                    text = stringResource(id = R.string.label_my_terms),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
            repeat(interests.size) { i ->
                Text(text =
                interests[i].start.toFormattedString(frontendFormatter),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmationPreview() {
    AttendanceTheme {
        UserTerms(
            userTermsUiState = UserTermsUiState.NewInterest(
                    interests = listOf(Interest(60, 500, 1, ZonedDateTime.now(), true)),
                    interestsToReg = listOf(Interest(60, 500, 1, ZonedDateTime.now(), false))
            ),
            onRegTermClick = {},
            onTermSelect = {},
            onDismissRequest = {},
            onError = {}
        )
    }
}