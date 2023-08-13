package eu.matoosh.attendance.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.repo.BookRepository
import eu.matoosh.attendance.repo.LoginRepository
import eu.matoosh.attendance.repo.RepoBookErrorCode
import eu.matoosh.attendance.repo.RepoBookResponse
import eu.matoosh.attendance.repo.RepoCheckResponse
import eu.matoosh.attendance.repo.RepoLoginResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

enum class BookErrorCode() {
    SERVER_UNAVAILABLE,
    TOKEN_EXPIRED,
    RENTAL_NOT_FOUND,
    APP_ERROR,
    UNKNOWN_ERROR
}

sealed interface BookUiState {
    data class Idle(val users: List<User>) : BookUiState
    data class Error(val message: String, val errorCode: BookErrorCode) : BookUiState
    data class Confirmation(val user: User) : BookUiState
    object Manual : BookUiState
    object Loading : BookUiState
    object Success : BookUiState
    object Init : BookUiState
}

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepo: BookRepository,
    private val loginRepo: LoginRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _bookUiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val bookUiState = _bookUiState.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())

    private val token: String?
        get() = sessionManager.token

    private var selectedUser: User? = null

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _bookUiState.value = loadUsersInternal()
        }
    }

    private suspend fun loadUsersInternal(): BookUiState {
        try {
            return if (token == null) {
                BookUiState.Error("Token already expired", BookErrorCode.TOKEN_EXPIRED)
            } else {
                when (val response = bookRepo.uncheckedList(token!!)) {
                    is RepoBookResponse.Success -> {
                        viewModelScope.launch {
                            delay(2000)
                            val currState = _bookUiState.value
                            if (currState is BookUiState.Idle) {
                                val newUiState = loadUsersInternal()
                                if ((newUiState as BookUiState.Idle).users.size != currState.users.size) {
                                    _bookUiState.value = newUiState
                                }
                            }
                        }
                        val screenUsers = if (response.users.isEmpty()) {
                            emptyList()
                        }
                        else {
                            response.users + User(-1, "Přidat manuálně", "", 0)
                        }
                        _users.value = screenUsers
                        BookUiState.Idle(screenUsers)
                    }

                    is RepoBookResponse.Error -> {
                        if (response.errorCode == RepoBookErrorCode.MISSING_RENTAL) {
                            BookUiState.Error(response.message, BookErrorCode.RENTAL_NOT_FOUND)
                        }
                        else {
                            BookUiState.Error(response.message, BookErrorCode.UNKNOWN_ERROR)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            return BookUiState.Error("IOException", BookErrorCode.UNKNOWN_ERROR)
        } catch (e: HttpException) {
            return BookUiState.Error("HttpException", BookErrorCode.SERVER_UNAVAILABLE)
        }
    }

    fun selectUser(username: String) {
        val users = (_bookUiState.value as? BookUiState.Idle)?.users ?: emptyList()

        selectedUser = users.firstOrNull { it.username == username }
        if (selectedUser != null) {
            _bookUiState.value = BookUiState.Confirmation(selectedUser!!)
        }
        else {
            _bookUiState.value = BookUiState.Error("No user has been selected", BookErrorCode.APP_ERROR)
        }
    }

    fun checkSelectedUser(user: User? = selectedUser) {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Success
            try {
                if (token == null) {
                    _bookUiState.value = BookUiState.Error("Token already expired", BookErrorCode.TOKEN_EXPIRED)
                }
                when (val response = bookRepo.check(token!!, user!!)) {
                    is RepoCheckResponse.Success -> {
                        val newBookUiState = viewModelScope.async { loadUsersInternal() }
                        val delayJob = viewModelScope.launch { delay(1500) }
                        delayJob.join()
                        _bookUiState.value = newBookUiState.await()
                    }
                    is RepoCheckResponse.Error -> {
                        if (response.errorCode == RepoBookErrorCode.MISSING_RENTAL) {
                            _bookUiState.value = BookUiState.Error(response.message, BookErrorCode.RENTAL_NOT_FOUND)
                        }
                        else {
                            _bookUiState.value = BookUiState.Error(response.message, BookErrorCode.UNKNOWN_ERROR)
                        }
                    }
                }
            } catch (e: IOException) {
                _bookUiState.value = BookUiState.Error("IOException", BookErrorCode.UNKNOWN_ERROR)
            } catch (e: HttpException) {
                _bookUiState.value = BookUiState.Error("HttpException", BookErrorCode.SERVER_UNAVAILABLE)
            }
        }
    }

    fun checkUnknownUser() {
        _bookUiState.value = BookUiState.Manual
    }

    fun checkUserManually(username: String, password: String) {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Loading
            try {
                when (val response = loginRepo.login(username, password)) {
                    is RepoLoginResponse.Success -> {
                        val userToBeCheckedIn = _users.value.firstOrNull { it.username == username }
                        checkSelectedUser(userToBeCheckedIn)
                        return@launch
                    }
                    is RepoLoginResponse.Error -> {
                        _bookUiState.value = BookUiState.Error(response.message, BookErrorCode.UNKNOWN_ERROR)
                    }
                }
            } catch (e: IOException) {
                LoginUiState.Error("IOException")
            } catch (e: HttpException) {
                LoginUiState.Error("HttpException")
            }
        }
    }

    fun cancel() {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Idle(_users.value)
            loadUsersInternal() // Starts auto-refresh feature
        }
    }
}