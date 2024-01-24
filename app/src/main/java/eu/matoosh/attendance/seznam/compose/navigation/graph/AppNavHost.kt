package eu.matoosh.attendance.seznam.compose.navigation.graph

import android.util.Log
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.navigation.global.ModeSelectionRoute
import eu.matoosh.attendance.seznam.compose.navigation.global.BookListRoute
import eu.matoosh.attendance.seznam.compose.navigation.global.bookDetailScreen
import eu.matoosh.attendance.seznam.compose.navigation.global.bookListScreen
import eu.matoosh.attendance.seznam.compose.navigation.global.navigateToBookDetail
import eu.matoosh.attendance.theme.AttendanceTheme

@Composable
fun AppNavHost(
    startDestination: String = BookListRoute
) {
    val navController = rememberNavController()

    Box {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = stringResource(id = R.string.content_description_background_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background_overlay))
        )
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(150, delayMillis = 150, easing = EaseOut)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(150, easing = EaseIn)
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(150, delayMillis = 150, easing = EaseIn)
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(150, easing = EaseOut)
                )
            }
        ) {
            bookListScreen(
                onNavigateToBookDetail = { bookId ->
                    Log.d("malytest", bookId)
                    navController.navigateToBookDetail(bookId)
                }
            )

            bookDetailScreen()
        }
    }
}

@Preview
@Composable
private fun AppNavHostPreview() {
    AttendanceTheme {
        AppNavHost(
            startDestination = ModeSelectionRoute
        )
    }
}