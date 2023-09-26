package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.Interest
import eu.matoosh.attendance.data.User
import eu.matoosh.attendance.di.IoDispatcher
import eu.matoosh.attendance.utils.toDateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import javax.inject.Inject

enum class RepoConsoleErrorCode(val serverField: String?) {
    INVALID_TOKEN("invalid-token"),
    INSUFFICIENT_CREDIT("insufficient-credit"),
    UNKNOWN_ERROR(null)
}

sealed interface RepoProfileResponse {
    data class Success(val profile: User) : RepoProfileResponse
    data class Error(val error: RepoConsoleErrorCode) : RepoProfileResponse
}

sealed interface RepoInterestsResponse {
    data class Success(val interests: List<Interest>) : RepoInterestsResponse
    data class Error(val error: RepoConsoleErrorCode) : RepoInterestsResponse
}

sealed interface RepoRegisterTermResponse {
    object Success : RepoRegisterTermResponse
    data class Error(val error: RepoConsoleErrorCode) : RepoRegisterTermResponse
}

class ConsoleRepository @Inject constructor(
    private val service: IceAppService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun profile(token: String): RepoProfileResponse = withContext(defaultDispatcher) {
        val response = service.profile(token)
        if (response.error == null) {
            RepoProfileResponse.Success(User(0, response.username!!, response.email!!, response.credit!!))
        }
        else {
            val errorCode = RepoConsoleErrorCode.values().find { it.serverField == response.error }
            RepoProfileResponse.Error(errorCode ?: RepoConsoleErrorCode.UNKNOWN_ERROR)
        }
    }

    suspend fun interests(token: String): RepoInterestsResponse = withContext(defaultDispatcher) {
        val response = service.interests(token)
        if (response.error == null) {
            RepoInterestsResponse.Success(response.interests?.map {
                Interest(it.duration!!,
                    it.price!!,
                    it.rentalId!!,
                    it.start!!.toDateTime(),
                    it.registered == 1
                )
            } ?: emptyList())
        }
        else {
            val errorCode = RepoConsoleErrorCode.values().find { it.serverField == response.error }
            RepoInterestsResponse.Error(errorCode ?: RepoConsoleErrorCode.UNKNOWN_ERROR)
        }
    }

    suspend fun registeTerm(token: String, rentalId: Int): RepoRegisterTermResponse = withContext(defaultDispatcher) {
        val response = service.registerTerm(token, rentalId)
        if (response.error == null) {
            RepoRegisterTermResponse.Success
        }
        else {
            val errorCode = RepoConsoleErrorCode.values().find { it.serverField == response.error }
            RepoRegisterTermResponse.Error(errorCode ?: RepoConsoleErrorCode.UNKNOWN_ERROR)
        }
    }
}