package eu.matoosh.attendance.seznam.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.Device
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.repo.LoginRepository
import eu.matoosh.attendance.repo.RepoLoginErrorCode
import eu.matoosh.attendance.repo.RepoLoginResponse
import eu.matoosh.attendance.seznam.data.Book
import eu.matoosh.attendance.seznam.repo.BookRepository
import eu.matoosh.attendance.seznam.repo.RepoBooksResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
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
    private val _bookUiState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val bookUiState = _bookUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _bookUiState.value = BookListUiState.Loading
            try {
                when (val response = repo.getBooks("Karel")) {
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
    }
}