package eu.matoosh.attendance.seznam.compose.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import eu.matoosh.attendance.R
import eu.matoosh.attendance.seznam.data.Book

@Composable
fun BookRow(
    book: Book,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.clickable(
            onClick = onClick
        )
    ) {
        val (image, title, author) = createRefs()
        AsyncImage(
            model = book.thumbnailUrl,
            contentDescription = stringResource(id = R.string.content_description_book_thumbnail),
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .height(64.dp)
                .width(64.dp)
        )
        Text(
            text = book.title,
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(image.end)
                    top.linkTo(parent.top)
                }
        )
        Text(
            text = book.author,
            modifier = Modifier
                .constrainAs(author) {
                    start.linkTo(image.end)
                    top.linkTo(title.bottom)
                }
        )
    }
}