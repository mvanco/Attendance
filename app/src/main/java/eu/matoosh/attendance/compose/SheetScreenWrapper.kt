package eu.matoosh.attendance.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import eu.matoosh.attendance.viewmodels.BookViewModel

@Composable
fun SheetScreenWrapper(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    bookViewModel: BookViewModel = hiltViewModel()
) {
    SheetScreen(bookViewModel)
}