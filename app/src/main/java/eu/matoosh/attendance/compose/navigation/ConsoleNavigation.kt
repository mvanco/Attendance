package eu.matoosh.attendance.compose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.AdminCredits
import eu.matoosh.attendance.compose.Icon
import eu.matoosh.attendance.compose.USER_DESTINATIONS
import eu.matoosh.attendance.compose.UserProfileScreen
import eu.matoosh.attendance.compose.UserRoute
import eu.matoosh.attendance.compose.UserScanner
import eu.matoosh.attendance.compose.UserTerms
import eu.matoosh.attendance.viewmodels.console.AdminCreditsViewModel
import eu.matoosh.attendance.viewmodels.console.UserProfileViewModel
import eu.matoosh.attendance.viewmodels.console.UserScannerViewModel
import kotlinx.coroutines.launch

const val ConsoleRoute = "console"
private const val isAdminArg = "isAdmin"

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.consoleScreen(
    onNavigateToLogin: () -> Unit
) {
    composable(
        "$ConsoleRoute?$isAdminArg={$isAdminArg}",
        arguments = listOf(navArgument(isAdminArg) {
            type = NavType.BoolType
            defaultValue = false
        })
    ) { backStackEntry ->
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Text(stringResource(id = R.string.app_name), fontSize = 26.sp, modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(R.string.action_logout)) },
                        selected = false,
                        onClick = {
                            onNavigateToLogin()
                        }
                    )
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.app_name)) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        navigationIcon = {
                            val coroutineScope = rememberCoroutineScope()
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (drawerState.isOpen) {
                                            drawerState.close()
                                        }
                                        else {
                                            drawerState.open()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    )
                }
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    var route by rememberSaveable { mutableStateOf(USER_DESTINATIONS.first().route) }
                    if (backStackEntry.arguments?.getBoolean("isAdmin") == true) {
                        val adminCreditsViewModel = hiltViewModel<AdminCreditsViewModel>()
                        AdminCredits(
                            viewModel = adminCreditsViewModel
                        )
                    }
                    else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            when (route) {
                                UserRoute.PROFILE -> {
                                    val userProfileViewModel =
                                        hiltViewModel<UserProfileViewModel>()
                                    UserProfileScreen(
                                        userProfileViewModel,
                                        onFailure = {}
                                    )
                                    LaunchedEffect(route) {
                                        userProfileViewModel.loadProfile()
                                    }
                                }

                                UserRoute.TERMS -> {
                                    UserTerms()
                                }

                                UserRoute.SCANNER -> {
                                    val userScannerViewModel =
                                        hiltViewModel<UserScannerViewModel>()
                                    UserScanner(
                                        userScannerViewModel,
                                        onSuccess = {
                                            route = UserRoute.PROFILE
                                        }
                                    )
                                    LaunchedEffect(route) {
                                        userScannerViewModel.initialize()
                                    }
                                }
                            }
                        }
                    }
                    if (backStackEntry.arguments?.getBoolean("isAdmin") == true) {

                    }
                    else {
                        NavigationBar(modifier = Modifier.fillMaxWidth()) {
                            USER_DESTINATIONS.forEach { destination ->
                                NavigationBarItem(
                                    selected = destination.route == route,
                                    onClick = {
                                        route = destination.route
                                    },
                                    icon = {
                                        when (destination.icon) {
                                            is Icon.IconImageVector -> {
                                                Icon(
                                                    imageVector = destination.icon.vector!!,
                                                    contentDescription = stringResource(id = destination.titleId)
                                                )
                                            }
                                            is Icon.IconVectorAsset -> {
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(id = destination.icon.iconRes),
                                                    contentDescription = stringResource(id = destination.titleId)
                                                )
                                            }
                                            else -> {}
                                        }

                                    },
                                    label = {
                                        Text(text = stringResource(id = destination.titleId))
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}

fun NavController.navigateLoginToConsole(isAdmin: Boolean = false) {
    val arg = if (isAdmin) "true" else "false"
    this.navigate("$ConsoleRoute?$isAdminArg=$arg") {
        popUpTo(LoginRoute) {
            inclusive = true
        }
    }
}

fun NavController.navigateModeSelectionToConsole(isAdmin: Boolean = false) {
    val arg = if (isAdmin) "true" else "false"
    navigate("$ConsoleRoute?$isAdminArg=$arg")
}

internal class ConsoleArgs(val isAdmin: Boolean) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(checkNotNull(savedStateHandle[isAdminArg]) as Boolean)
}