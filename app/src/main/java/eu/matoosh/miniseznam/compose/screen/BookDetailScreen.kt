package eu.matoosh.miniseznam.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import eu.matoosh.miniseznam.R
import eu.matoosh.miniseznam.compose.widget.FullScreenMessage
import eu.matoosh.miniseznam.data.Book
import eu.matoosh.miniseznam.viewmodel.BookDetailUiState
import eu.matoosh.miniseznam.viewmodel.BookDetailViewModel

@Composable
fun BookDetailScreen(
    id: String,
    bookDetail: BookDetailViewModel = hiltViewModel()
) {
    val bookUiState by bookDetail.bookUiState.collectAsState()
    when (val state = bookUiState) {
        is BookDetailUiState.Loading -> {
            FullScreenMessage(stringResource(id = R.string.message_booklist_loading))
        }

        is BookDetailUiState.Idle -> {
            BookDetailScreen(state.book)
        }

        is BookDetailUiState.Error -> {

        }
    }

    LaunchedEffect(Unit) {
        bookDetail.initialize(id)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookDetailScreen(
    book: Book
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_overlay))
            .padding(32.dp)
    ) {

        Row() {
            GlideImage(
                modifier = Modifier
                    .size(64.dp)
                    .padding(
                        end = 16.dp
                    ),
                model = book.imageUrl,
                contentDescription = stringResource(id = R.string.content_description_book_thumbnail),
                failure = placeholder(R.drawable.ic_image_placeholder),
                loading = placeholder(R.drawable.ic_image_placeholder)
            )

            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = Bold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${book.author}, ${book.publishedDate}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Column (
            modifier = Modifier
                .heightIn(
                    max = 300.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = book.description ?: "N/A",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        val uriHandler = LocalUriHandler.current
        val annotatedText = if (book.googlePlayLink == null) {
            buildAnnotatedString {
                append("Google Play odkaz není k dispozici")
            }
        }
        else {
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    appendLink("Otevři na Google Play", book.googlePlayLink )
                }
            }
        }
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
            },
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        val annotatedText2 = if (book.webReaderLink == null) {
            buildAnnotatedString {
                append("Web čtečka není dostupná")
            }
        }
        else {
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    appendLink("Otevři ve Web čtečce", book.webReaderLink )
                }
            }
        }
        ClickableText(
            text = annotatedText2,
            onClick = { offset ->
                annotatedText2.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
            },
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

fun AnnotatedString.Builder.appendLink(linkText: String, linkUrl: String) {
    pushStringAnnotation(tag = "URL", annotation = linkUrl)
    append(linkText)
    pop()
}