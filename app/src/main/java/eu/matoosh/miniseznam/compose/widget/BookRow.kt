package eu.matoosh.miniseznam.compose.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import eu.matoosh.miniseznam.R
import eu.matoosh.miniseznam.data.Book
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookRow(
    book: Book,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .fillMaxWidth()
    ) {
        val (image, title, author) = createRefs()
        GlideImage(
            modifier = Modifier
                .size(64.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
            },
            model = book.thumbnailUrl,
            contentDescription = stringResource(id = R.string.content_description_book_thumbnail),
            failure = placeholder(R.drawable.ic_image_placeholder),
            loading = placeholder(R.drawable.ic_image_placeholder)
        )
        Text(
            text = book.title,
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(image.end)
                    top.linkTo(parent.top, margin = 8.dp)
                }
                .width(300.dp),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.author,
            modifier = Modifier
                .constrainAs(author) {
                    start.linkTo(image.end)
                    top.linkTo(title.bottom)
                }
                .width(300.dp),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}