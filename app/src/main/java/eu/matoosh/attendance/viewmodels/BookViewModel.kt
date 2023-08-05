package eu.matoosh.attendance.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.repo.BookRepository
import eu.matoosh.attendance.repo.RepoBookResponse
import eu.matoosh.attendance.repo.RepoCheckResponse
import eu.matoosh.attendance.repo.RepoLoginResponse
import eu.matoosh.attendance.utils.asImmutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface BookUiState {
    data class Error(val message: String) : BookUiState
    data class Confirmation(val user: User) : BookUiState
    object Loading : BookUiState
    object Idle : BookUiState
    object Success : BookUiState
}

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repo: BookRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _bookUiState = MutableLiveData<BookUiState>()
    val bookUiState = _bookUiState.asImmutable()

    private val _userList = MutableLiveData<List<User>>()
    val userList = _userList.asImmutable()

    private val token: String?
        get() = sessionManager.token

    private var selectedUser: User? = null

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Loading
            _bookUiState.value = try {
                if (token == null) {
                    BookUiState.Error("Token already expired")
                }
                when (val response = repo.uncheckedList(token!!)) {
                    is RepoBookResponse.Success -> {
                        _userList.value = response.users
                        BookUiState.Idle
                    }
                    is RepoBookResponse.Error -> BookUiState.Error(response.message)
                }
            } catch (e: IOException) {
                BookUiState.Error("IOException")
            } catch (e: HttpException) {
                BookUiState.Error("HttpException")
            }
        }
    }

    fun selectUser(username: String) {
        selectedUser = userList.value?.firstOrNull { it.username == username }
        if (selectedUser != null) {
            _bookUiState.value = BookUiState.Confirmation(selectedUser!!)
        }
        else {
            _bookUiState.value = BookUiState.Error("No user has been selected")
        }
    }

    fun checkSelectedUser() {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Loading
            try {
                if (token == null) {
                    _bookUiState.value = BookUiState.Error("Token already expired")
                }
                when (val response = repo.check(token!!, selectedUser!!)) {
                    is RepoCheckResponse.Success -> {
                        _bookUiState.value = BookUiState.Success
                        val job1 = launch(Dispatchers.Default) { loadUsers() }
                        val job2 = launch(Dispatchers.Default) { delay(3000) }
                        job1.join()
                        job2.join()
                        _bookUiState.value = BookUiState.Idle
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