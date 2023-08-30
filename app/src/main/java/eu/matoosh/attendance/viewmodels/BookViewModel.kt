package eu.matoosh.attendance.viewmodels

import android.util.Log
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var selectedUser: Int? = null
    private var firstRun = true

    init {
        bindUsers()
        viewModelScope.launch {
            _users.collect { userList ->
                if (_bookUiState.value is BookUiState.Idle || firstRun) {
                    _bookUiState.value = BookUiState.Idle(userList)
                    firstRun = false
                }
            }
        }
    }

    private fun bindUsers() {
        viewModelScope.launch {
            try {
                if (token == null) {
                    _bookUiState.value =
                        BookUiState.Error("Token already expired", BookErrorCode.TOKEN_EXPIRED)
                }
                bookRepo.uncheckedListWithUpdates(token!!).collect { response ->
                    when (response) {
                        is RepoBookResponse.Success -> {
                            _users.value = response.users
                        }

                        is RepoBookResponse.Error -> {
                            when (response.errorCode) {
                                RepoBookErrorCode.MISSING_RENTAL -> {
                                    _bookUiState.value =
                                        BookUiState.Error(
                                            response.message,
                                            BookErrorCode.RENTAL_NOT_FOUND
                                        )
                                }

                                else -> {
                                    _bookUiState.value =
                                        BookUiState.Error(
                                            response.message,
                                            BookErrorCode.UNKNOWN_ERROR
                                        )
                                }
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                _bookUiState.value =
                    BookUiState.Error("IOException", BookErrorCode.UNKNOWN_ERROR)
            } catch (e: HttpException) {
                _bookUiState.value =
                    BookUiState.Error("HttpException", BookErrorCode.SERVER_UNAVAILABLE)
            }
        }
    }

    fun selectUser(username: String) {
        val users = (_bookUiState.value as? BookUiState.Idle)?.users ?: emptyList()

        val foundUser = users.firstOrNull { it.username == username }
        selectedUser = foundUser?.id
        if (selectedUser != null) {
            _bookUiState.value = BookUiState.Confirmation(foundUser!!)
        }
        else {
            _bookUiState.value = BookUiState.Error("No user has been selected", BookErrorCode.APP_ERROR)
        }
    }

    fun checkSelectedUser(userId: Int? = selectedUser) {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Success
            try {
                if (token == null) {
                    _bookUiState.value = BookUiState.Error("Token already expired", BookErrorCode.TOKEN_EXPIRED)
                    return@launch
                }
                val response = if (userId == null) {
                    RepoCheckResponse.Success(-1)
                }
                else {
                    bookRepo.check(token!!, userId)
                }
                when (response) {
                    is RepoCheckResponse.Success -> {
                        delay(1500)
                        _bookUiState.value = BookUiState.Idle(_users.value)
                    }
                    is RepoCheckResponse.Error -> {
                        when(response.errorCode) {
                            RepoBookErrorCode.MISSING_RENTAL -> {
                                _bookUiState.value = BookUiState.Error(response.message, BookErrorCode.RENTAL_NOT_FOUND)
                            }
                            RepoBookErrorCode.DUPLICATE_CHECKIN -> {
                                Log.d("security-check", "User with order ${response.order} is black passenger.")
                                delay(1500)
                                _bookUiState.value = BookUiState.Idle(_users.value)
                            }
                            else -> {
                                _bookUiState.value = BookUiState.Error(response.message, BookErrorCode.UNKNOWN_ERROR)
                            }
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
                        checkSelectedUser(response.userId)
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
        }
    }
}