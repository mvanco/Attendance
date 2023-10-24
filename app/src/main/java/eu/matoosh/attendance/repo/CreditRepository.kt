package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.ApiIssueAuthorizationResponse
import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface RepoCreditResponse {
    object Success : RepoCreditResponse
    data class Error(val error: RepoCreditErrorCode) : RepoCreditResponse
}

enum class RepoCreditErrorCode(val serverField: String?) {
    WRONG_USER("wrong-user"),
    UNKNOWN(null)
}

class CreditRepository @Inject constructor(
    private val service: IceAppService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun issueAuthorization(token: String, credit: Int): ApiIssueAuthorizationResponse = withContext(defaultDispatcher) {
        service.issueAuthorization(token, credit.toString())
    }

    suspend fun addCredit(token: String, authToken: String): RepoCreditResponse = withContext(defaultDispatcher) {
        val response = service.addCredit(token, authToken)
        if (response.error == null) {
            RepoCreditResponse.Success
        }
        else {
            val errorCode = RepoCreditErrorCode.values().find { it.serverField == response.error }
            RepoCreditResponse.Error(errorCode ?: RepoCreditErrorCode.UNKNOWN)
        }
    }
}