package eu.matoosh.miniseznam.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.miniseznam.data.Book
import eu.matoosh.miniseznam.repo.BookRepository
import eu.matoosh.miniseznam.repo.RepoBookDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class BookDetailErrorCode() {
    UNKNOWN_ERROR
}

@Stable
sealed interface BookDetailUiState {
    object Loading : BookDetailUiState
    data class Idle(val book: Book) : BookDetailUiState
    data class Error(val errorCode: Int) : BookDetailUiState
}

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repo: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _bookUiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val bookUiState = _bookUiState.asStateFlow()

    fun initialize(id: String) {
        viewModelScope.launch {
            try {
                when (val response = repo.getBookDetail(id)) {
                    is RepoBookDetailResponse.Success -> {
                        _bookUiState.value = BookDetailUiState.Idle(response.bookDetail)
                    }
                    is RepoBookDetailResponse.Error -> {
                        _bookUiState.value = BookDetailUiState.Error(500)
                    }
                }
            } catch (e: Exception) {
                _bookUiState.value = BookDetailUiState.Error(500)
            }
        }
    }
}