package eu.matoosh.attendance.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.matoosh.attendance.R
import eu.matoosh.attendance.compose.widget.FullScreenMessage
import eu.matoosh.attendance.compose.widget.BookRow
import eu.matoosh.attendance.data.Book
import eu.matoosh.attendance.viewmodel.BookListUiState
import eu.matoosh.attendance.viewmodel.BookListViewModel

@Composable
fun BookListScreen(
    bookViewModel: BookListViewModel = hiltViewModel(),
    onNavigateToBookDetail: (String) -> Unit
) {
    val bookUiState by bookViewModel.bookUiState.collectAsState()
    when (val state = bookUiState) {
        is BookListUiState.Idle -> {
            val query = bookViewModel.query.collectAsStateWithLifecycle()
            if (state.books.isEmpty()) {
                NoResultBookListScreen(
                    onClick = { query ->
                        bookViewModel.updateQuery(query)
                    },
                    query = query.value,
                    onQueryChange = { bookViewModel.mayUpdateQuery(it) },
                    showClearButton = true,
                    onClear = {
                        bookViewModel.updateQuery("")
                    }
                )
            }
            else {
                BookListScreen(
                    onClick = { query ->
                        bookViewModel.updateQuery(query)
                    },
                    query = query.value,
                    onQueryChange = { bookViewModel.mayUpdateQuery(it) },
                    books = state.books,
                    onBookClick = onNavigateToBookDetail
                )
            }
        }
        else -> {
            FullScreenMessage(stringResource(id = R.string.message_booklist_loading))
        }
    }
}

@Composable
fun BookListScreen(
    onClick: (String) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    books: List<Book>,
    onBookClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.background_overlay))
            .widthIn(max = dimensionResource(id = R.dimen.max_screen_width))
            .padding(vertical = 32.dp, horizontal = 16.dp)
            .fillMaxHeight()
    ) {
        Row() {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text(stringResource(R.string.label_author)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onClick(query) }
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Button(
                onClick = { onClick(query) },
                modifier = Modifier
                    .wrapContentWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(stringResource(R.string.action_search))
            }
        }
        Spacer(
            modifier = Modifier.height(32.dp)
        )
        LazyColumn() {
            items(
                items = books,
                key = { it.id }
            ) { book ->
                BookRow(
                    book = book,
                    onClick = { onBookClick(book.id) }
                )
            }
        }
    }
}