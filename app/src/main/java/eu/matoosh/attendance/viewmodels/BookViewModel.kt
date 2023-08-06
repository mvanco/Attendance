package eu.matoosh.attendance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.repo.BookRepository
import eu.matoosh.attendance.repo.RepoBookResponse
import eu.matoosh.attendance.repo.RepoCheckResponse
import eu.matoosh.attendance.utils.asImmutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface BookUiState {
    data class Idle(val users: List<User>) : BookUiState
    data class Error(val message: String) : BookUiState
    data class Confirmation(val user: User) : BookUiState
    object Loading : BookUiState
    object Success : BookUiState
    object Init : BookUiState
}

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repo: BookRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _bookUiState = MutableLiveData<BookUiState>()
    val bookUiState: LiveData<BookUiState>
        get() = _bookUiState

    private val token: String?
        get() = sessionManager.token

    private var selectedUser: User? = null


    fun loadUsers() {
        viewModelScope.launch {
            _bookUiState.value = loadUsersInternal()
        }
    }

    private suspend fun loadUsersInternal(): BookUiState {
        try {
            return if (token == null) {
                BookUiState.Error("Token already expired")
            } else {
                when (val response = repo.uncheckedList(token!!)) {
                    is RepoBookResponse.Success -> {
                        BookUiState.Idle(response.users)
                    }

                    is RepoBookResponse.Error -> {
                        BookUiState.Error(response.message)
                    }
                }
            }
        } catch (e: IOException) {
            return BookUiState.Error("IOException")
        } catch (e: HttpException) {
            return BookUiState.Error("HttpException")
        }
    }

    fun selectUser(username: String) {
        val users = (_bookUiState.value as? BookUiState.Idle)?.users ?: emptyList()

        selectedUser = users.firstOrNull { it.username == username }
        if (selectedUser != null) {
            _bookUiState.value = BookUiState.Confirmation(selectedUser!!)
        }
        else {
            _bookUiState.value = BookUiState.Error("No user has been selected")
        }
    }

    fun checkSelectedUser() {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Success
            try {
                if (token == null) {
                    _bookUiState.value = BookUiState.Error("Token already expired")
                }
                when (val response = repo.check(token!!, selectedUser!!)) {
                    is RepoCheckResponse.Success -> {
                        val newBookUiState = async(Dispatchers.Default) { loadUsersInternal() }
                        val delayJob = launch(Dispatchers.Default) { delay(3000) }
                        delayJob.join()
                        _bookUiState.value = newBookUiState.await()
                    }
                    is RepoCheckResponse.Error -> _bookUiState.value = BookUiState.Error(response.message)
                }
            } catch (e: IOException) {
                _bookUiState.value = BookUiState.Error("IOException")
            } catch (e: HttpException) {
                _bookUiState.value = BookUiState.Error("HttpException")
            }
        }
    }
}