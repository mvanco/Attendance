package eu.matoosh.attendance.compose.widget.global

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.AppBarActions
import kotlinx.coroutines.launch

@Composable
fun AttendanceAppBar(
    title: String,
    canNavigateUp: Boolean,
    navigateUp: () -> Unit,
    drawerState: DrawerState,
    appBarActions: AppBarActions,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if (canNavigateUp) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_back_button),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
            else {
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
                        contentDescription = stringResource(R.string.content_description_home_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            appBarActions.actions.forEach { appBarAction ->
                IconButton(onClick = appBarAction.onClickListener) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = appBarAction.icon),
                        contentDescription = stringResource(id = appBarAction.iconDescription),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    )
}