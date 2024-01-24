package eu.matoosh.attendance.seznam.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.config.AUTO_QUERY_MIN_CHAR
import eu.matoosh.attendance.config.QUERY_TIMEOUT
import eu.matoosh.attendance.seznam.data.Book
import eu.matoosh.attendance.seznam.repo.BookRepository
import eu.matoosh.attendance.seznam.repo.RepoBooksResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

enum class BookListErrorCode() {
    UNKNOWN_ERROR
}

@Stable
sealed interface BookListUiState {
    object Loading : BookListUiState
    data class Idle(val books: List<Book>) : BookListUiState
    data class Error(val errorCode: Int) : BookListUiState
}

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repo: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _bookUiState = MutableStateFlow<BookListUiState>(BookListUiState.Idle(emptyList()))
    val bookUiState = _bookUiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    init {
        viewModelScope.launch {
            _query.debounce(QUERY_TIMEOUT).filter { it.length >= AUTO_QUERY_MIN_CHAR }.collect {
                updateQueryInternal(it)
            }
        }
    }

    fun updateQuery(query: String) {
        viewModelScope.launch {
            _bookUiState.value = BookListUiState.Loading
            _query.value = query
            updateQueryInternal(query)
        }
    }

    private suspend fun updateQueryInternal(query: String) {
        if (query.isEmpty()) {
            _bookUiState.value = BookListUiState.Idle(emptyList())
        }
        try {
            when (val response = repo.getBooks(query)) {
                is RepoBooksResponse.Success -> {
                    _bookUiState.value = BookListUiState.Idle(response.books)
                }
                is RepoBooksResponse.Error -> {
                    _bookUiState.value = BookListUiState.Error(500)
                }
            }
        } catch (e: Exception) {
            _bookUiState.value = BookListUiState.Error(500)
        }
    }

    fun mayUpdateQuery(query: String) {
        _query.value = query
    }
}