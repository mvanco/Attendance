package eu.matoosh.attendance.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.screen.AdminConsoleScreen
import eu.matoosh.attendance.compose.screen.UserConsoleScreen
import eu.matoosh.attendance.compose.widget.user.AttendanceAppBar
import eu.matoosh.attendance.compose.screen.USER_DESTINATIONS
import eu.matoosh.attendance.compose.screen.findDest
import eu.matoosh.attendance.viewmodels.console.AdminCreditsViewModel

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
        Scaffold(
            topBar = {
                AttendanceAppBar(
                    title = stringResource(USER_DESTINATIONS.findDest(dest.value?.destination?.route)?.titleId ?: R.string.app_name),
                    canNavigateUp = userNavController.previousBackStackEntry != null,
                    navigateUp = { userNavController.navigateUp() },
                    drawerState = drawerState
                )

            },
            containerColor = Color.Transparent
        ) { contentPadding ->
            Box {
                if (isAdmin) {
                    val adminCreditsViewModel: AdminCreditsViewModel = hiltViewModel()
                    AdminConsoleScreen(
                        viewModel = adminCreditsViewModel
                    )
                } else {
                    UserConsoleScreen(userNavController)
                }
            }
        }
    }
}