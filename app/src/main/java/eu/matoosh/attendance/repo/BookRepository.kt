package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class RepoBookErrorCode(val serverField: String?) {
    INCOMPATIBLE_VERSIONS(null),
    MISSING_RENTAL("missing-rental"),
    DUPLICATE_CHECKIN("duplicate-checkin"),
    UNKNOWN_ERROR(null)
}

sealed interface RepoBookResponse {
    data class Success(val users: List<User>) : RepoBookResponse
    data class Error(val error: RepoBookErrorCode) : RepoBookResponse
}

sealed interface RepoCheckResponse {
    data class Success(val order: Int) : RepoCheckResponse
    data class Error(val error: RepoBookErrorCode, val order: Int? = null) : RepoCheckResponse
}

sealed interface RepoUncheckResponse {
    object Success : RepoUncheckResponse
    data class Error(val error: RepoBookErrorCode) : RepoUncheckResponse
}

class BookRepository @Inject constructor(
    private val service: IceAppService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun uncheckedList(token: String): RepoBookResponse = withContext(defaultDispatcher) {
        val response = service.uncheckedList(token)
        if (response.error == null) {
            if (response.uncheckedList != null) {
                RepoBookResponse.Success(response.uncheckedList.map {
                    if (it.id != null && it.username != null && it.email != null && it.credit != null) {
                        User(it.id, it.username, it.email, it.credit)
                    } else {
                        return@withContext RepoBookResponse.Error(RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
                    }
                })
            } else {
                RepoBookResponse.Error(RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
            }
        } else {
            val errorCode = RepoBookErrorCode.values().find { it.serverField == response.error }
            RepoBookResponse.Error(errorCode ?: RepoBookErrorCode.UNKNOWN_ERROR)
        }
    }

    fun uncheckedListWithUpdates(token: String) = flow {
        var shouldFinish = false
        while(!shouldFinish) {
            val response = service.uncheckedList(token)
            if (response.error == null) {
                if (response.uncheckedList != null) {
                    val users = response.uncheckedList.map {
                        if (it.id != null && it.username != null && it.email != null && it.credit != null) {
                            User(it.id, it.username, it.email, it.credit)
                        }
                        else {
                            val error = RepoBookResponse.Error(RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
                            emit(error)
                            shouldFinish = true
                            User(-1, "", "", 0)
                        }
                    }
                    if (!shouldFinish) {
                        emit(RepoBookResponse.Success(users))
                    }
                } else {
                    val error = RepoBookResponse.Error(RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
                    emit(error)
                    shouldFinish = true
                }
            } else {
                val errorCode = RepoBookErrorCode.values().find { it.serverField == response.error }
                val error = RepoBookResponse.Error(errorCode ?: RepoBookErrorCode.UNKNOWN_ERROR)
                emit(error)
                shouldFinish = true
            }
            delay(2000)
        }
    }.flowOn(defaultDispatcher)


    suspend fun check(token: String, userId: Int): RepoCheckResponse = withContext(defaultDispatcher) {
        val response = service.check(token, userId.toString())
        if (response.error == null) {
            if (response.order != null) {
                RepoCheckResponse.Success(response.order)
            } else {
                RepoCheckResponse.Error(RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
            }
        } else {
            val errorCode = RepoBookErrorCode.values().find { it.serverField == response.error }
            RepoCheckResponse.Error(errorCode ?: RepoBookErrorCode.UNKNOWN_ERROR, response.order)
        }
    }
}