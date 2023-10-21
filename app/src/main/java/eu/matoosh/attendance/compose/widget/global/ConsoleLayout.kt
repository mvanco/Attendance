package eu.matoosh.attendance.compose.widget.global

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.screen.AdminConsoleScreen
import eu.matoosh.attendance.compose.screen.USER_DESTINATIONS
import eu.matoosh.attendance.compose.screen.UserConsoleScreen
import eu.matoosh.attendance.compose.screen.findDest
import eu.matoosh.attendance.config.UI_DEBOUNCE_TIMEOUT_LONG_MS
import eu.matoosh.attendance.config.UI_DEBOUNCE_TIMEOUT_SHORT_MS
import eu.matoosh.attendance.viewmodels.console.AdminCreditsViewModel
import kotlinx.coroutines.flow.debounce

data class FabState(
    val isVisible: Boolean = false,
    val onClickListener: () -> Unit = {},
    @DrawableRes val icon: Int = R.drawable.ic_create,
    @StringRes val iconDescriptionn: Int = R.string.content_description_register_term_fab,
    @StringRes val text: Int = R.string.label_scanner,
)

data class OnFabStateChange(
    val onChange: (FabState) -> Unit
)

val LocalOnFabStateChange = compositionLocalOf { OnFabStateChange {} }

data class AppBarAction(
    val onClickListener: () -> Unit = {},
    @DrawableRes val icon: Int = R.drawable.ic_world,
    @StringRes val iconDescription: Int = R.string.label_scanner
)

data class AppBarActions(
    val actions: List<AppBarAction> = emptyList()
)

data class OnAppBarActionsChange(
    val onChange: (AppBarActions) -> Unit
)

val LocalOnAppBarActionsChange = compositionLocalOf { OnAppBarActionsChange {} }

@Composable
fun ConsoleLayout(
    isAdmin: Boolean,
    onNavigateAdminToLogin: () -> Unit,
    onNavigateUserToLogin: () -> Unit
) {
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
                        if (isAdmin) {
                            onNavigateAdminToLogin()
                        }
                        else {
                            onNavigateUserToLogin()
                        }
                    }
                )
            }
        },
        drawerState = drawerState
    ) {
        val userNavController: NavHostController = rememberNavController()
        val dest = userNavController.currentBackStackEntryAsState()
        var fabState by remember { mutableStateOf(FabState()) }
        var fabStateDebounced by remember { mutableStateOf(FabState()) }
        var appBarActions by remember { mutableStateOf(AppBarActions()) }
        var appBarActionsDebounced by remember { mutableStateOf(AppBarActions()) }

        LaunchedEffect(Unit) {
            snapshotFlow { fabState }.debounce(UI_DEBOUNCE_TIMEOUT_SHORT_MS).collect {
                fabStateDebounced = it
            }
        }

        LaunchedEffect(Unit) {
            snapshotFlow { appBarActions }.debounce(UI_DEBOUNCE_TIMEOUT_LONG_MS).collect {
                appBarActionsDebounced = it
            }
        }

        LaunchedEffect(userNavController) {
            userNavController.currentBackStackEntryFlow.collect { _ ->
                fabState = FabState()
                appBarActions = AppBarActions()
            }
        }

        Scaffold(
            topBar = {
                AttendanceAppBar(
                    title = stringResource(USER_DESTINATIONS.findDest(dest.value?.destination?.route)?.titleId ?: R.string.app_name),
                    canNavigateUp = userNavController.previousBackStackEntry != null,
                    navigateUp = { userNavController.navigateUp() },
                    appBarActions = appBarActionsDebounced,
                    drawerState = drawerState
                )

            },
            floatingActionButton = {
                if (fabStateDebounced.isVisible) {
                    ExtendedFloatingActionButton(
                        onClick = fabStateDebounced.onClickListener,
                        icon = {
                            Icon(
                                ImageVector.vectorResource(id = fabStateDebounced.icon),
                                contentDescription = stringResource(fabStateDebounced.iconDescriptionn)
                            )
                        },
                        text = {
                            Text(text = stringResource(fabStateDebounced.text))
                        },
                        modifier = Modifier.offset(
                            y = dimensionResource(id = R.dimen.fab_bottom_margin)
                        )
                    )
                }
            },
            containerColor = Color.Transparent
        ) { contentPadding ->
            Box(
                modifier = Modifier.padding(contentPadding)
            ) {
                if (isAdmin) {
                    val adminCreditsViewModel: AdminCreditsViewModel = hiltViewModel()
                    AdminConsoleScreen(
                        viewModel = adminCreditsViewModel
                    )
                } else {
                    CompositionLocalProvider(
                        LocalOnFabStateChange provides OnFabStateChange { fabState = it },
                        LocalOnAppBarActionsChange provides OnAppBarActionsChange { appBarActions = it }
                    ) {
                        UserConsoleScreen(
                            userNavController
                        )
                    }
                }
            }
        }
    }
}