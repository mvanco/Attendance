package eu.matoosh.attendance.compose.screen.page.user

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.AppBarAction
import eu.matoosh.attendance.compose.AppBarActions
import eu.matoosh.attendance.compose.FabState
import eu.matoosh.attendance.compose.LocalOnAppBarActionsChange
import eu.matoosh.attendance.compose.LocalOnFabStateChange
import eu.matoosh.attendance.compose.screen.page.Message
import eu.matoosh.attendance.compose.widget.user.InterestDialog
import eu.matoosh.attendance.config.WEB_APP_URL
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
    val registrationEnabled by userTermsViewModel.registrationEnabled.collectAsState()

    val onAppBarActionsChanged = LocalOnAppBarActionsChange.current.onChange
    val ctx = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        onAppBarActionsChanged(
            AppBarActions(listOf(
                AppBarAction(
                    onClickListener = {
                        val webpage: Uri = Uri.parse(WEB_APP_URL)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        ctx.startActivity(intent)
                    },
                    icon = R.drawable.ic_world
                )
            ))
        )
    }

    val onFabStateChange = LocalOnFabStateChange.current.onChange
    LaunchedEffect(key1 = registrationEnabled) {
        onFabStateChange(FabState(
            isVisible = registrationEnabled,
            onClickListener = {
                userTermsViewModel.selectTerm()
            },
            icon = R.drawable.ic_create,
            iconDescriptionn = R.string.content_description_register_term_fab,
            text = R.string.action_register_term)
        )
    }

    UserTerms(
        userTermsUiState = userTermsUiState,
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
    onTermSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onError: () -> Unit
) {
    when (userTermsUiState) {
        is UserTermsUiState.Idle -> {
            UserTerms(
                userTermsUiState.interests
            )
        }
        is UserTermsUiState.NewInterest -> {
            UserTerms(
                userTermsUiState.interests
            )
            InterestDialog(userTermsUiState.interestsToReg, onTermSelect, onDismissRequest)
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
    }
}

@Composable
fun UserTerms(
    interests: List<Interest>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(colorResource(id = R.color.background_overlay)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (interests.isEmpty()) {
            Text(
                text = stringResource(id = R.string.label_my_terms),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(id = R.string.message_user_no_interest),
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            Text(
                text = stringResource(id = R.string.label_my_terms),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        if (interests.isNotEmpty()) {
            Divider(
                color = MaterialTheme.colorScheme.outline
            )
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .padding(8.dp)
            ) {
                items(interests) { interest ->
                    Text(
                        text =
                        interest.start.toFormattedString(frontendFormatter),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Divider(
                color = MaterialTheme.colorScheme.outline
            )
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
            onTermSelect = {},
            onDismissRequest = {},
            onError = {}
        )
    }
}