package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    data class Error(val message: String, val errorCode: RepoBookErrorCode) : RepoBookResponse
}

sealed interface RepoCheckResponse {
    data class Success(val order: Int) : RepoCheckResponse
    data class Error(val message: String, val errorCode: RepoBookErrorCode, val order: Int? = null) : RepoCheckResponse
}

sealed interface RepoUncheckResponse {
    object Success : RepoUncheckResponse
    data class Error(val message: String, val errorCode: RepoBookErrorCode) : RepoUncheckResponse
}

class BookRepository @Inject constructor(
    private val service: IceAppService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun uncheckedList(token: String): RepoBookResponse = withContext(defaultDispatcher) {
        val response = service.uncheckedList(token)
        if (response.errorCode == null) {
            if (response.uncheckedList != null) {
                RepoBookResponse.Success(response.uncheckedList.map {
                    if (it.id != null && it.username != null && it.email != null && it.credit != null) {
                        User(it.id, it.username, it.email, it.credit)
                    } else {
                        return@withContext RepoBookResponse.Error(
                            "Incompatible API version!",
                            RepoBookErrorCode.INCOMPATIBLE_VERSIONS
                        )
                    }
                })
            } else {
                RepoBookResponse.Error(
                    "Incompatible API version!",
                    RepoBookErrorCode.INCOMPATIBLE_VERSIONS
                )
            }
        } else {
            if (response.errorCode == RepoBookErrorCode.MISSING_RENTAL.serverField) {
                RepoBookResponse.Error(
                    response.error ?: "Unknown message",
                    RepoBookErrorCode.MISSING_RENTAL
                )
            } else {
                RepoBookResponse.Error(
                    response.error ?: "Unknown error",
                    RepoBookErrorCode.UNKNOWN_ERROR
                )
            }
        }
    }

    fun uncheckedListWithUpdates(token: String) = flow {
        var shouldFinish = false
        while(!shouldFinish) {
            val response = service.uncheckedList(token)
            if (response.errorCode == null) {
                if (response.uncheckedList != null) {
                    val users = response.uncheckedList.map {
                        if (it.id != null && it.username != null && it.email != null && it.credit != null) {
                            User(it.id, it.username, it.email, it.credit)
                        }
                        else {
                            val error = RepoBookResponse.Error("Incompatible API version!", RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
                            emit(error)
                            shouldFinish = true
                            User(-1, "", "", 0)
                        }
                    }
                    if (!shouldFinish) {
                        emit(RepoBookResponse.Success(users))
                    }
                } else {
                    val error = RepoBookResponse.Error(
                        "Incompatible API version!",
                        RepoBookErrorCode.INCOMPATIBLE_VERSIONS
                    )
                    emit(error)
                    shouldFinish = true
                }
            } else {
                if (response.errorCode == RepoBookErrorCode.MISSING_RENTAL.serverField) {
                    val error = RepoBookResponse.Error(
                        response.error ?: "Unknown message",
                        RepoBookErrorCode.MISSING_RENTAL
                    )
                    emit(error)
                } else {
                    val error = RepoBookResponse.Error(
                        response.error ?: "Unknown error",
                        RepoBookErrorCode.UNKNOWN_ERROR
                    )
                    emit(error)
                }
                shouldFinish = true
            }
            delay(2000)
        }
    }.flowOn(defaultDispatcher)


    suspend fun check(token: String, userId: Int): RepoCheckResponse = withContext(defaultDispatcher) {
        val response = service.check(token, userId.toString())
        if (response.errorCode == null) {
            if (response.result != null && response.result == "Success" && response.order != null) {
                RepoCheckResponse.Success(response.order)
            } else {
                RepoCheckResponse.Error("Incompatible API version!", RepoBookErrorCode.INCOMPATIBLE_VERSIONS)
            }
        } else {
            if (response.errorCode == RepoBookErrorCode.MISSING_RENTAL.serverField) {
                RepoCheckResponse.Error(response.error ?: "Unknown message", RepoBookErrorCode.MISSING_RENTAL)
            }
            else if (response.errorCode == RepoBookErrorCode.DUPLICATE_CHECKIN.serverField) {
                RepoCheckResponse.Error(response.error ?: "Unknown message", RepoBookErrorCode.DUPLICATE_CHECKIN, response.order)
            }
            else {
                RepoCheckResponse.Error(
                    response.error ?: "Unknown error",
                    RepoBookErrorCode.UNKNOWN_ERROR
                )
            }
        }
    }
}