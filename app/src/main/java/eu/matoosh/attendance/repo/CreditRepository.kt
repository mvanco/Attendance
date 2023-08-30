package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.ApiAddCreditResponse
import eu.matoosh.attendance.api.ApiIssueAuthorizationResponse
import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreditRepository @Inject constructor(
    private val service: IceAppService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun issueAuthorization(token: String, credit: Int): ApiIssueAuthorizationResponse = withContext(defaultDispatcher) {
        service.issueAuthorization(token, credit.toString())
    }

    suspend fun addCredit(token: String, authToken: String): ApiAddCreditResponse = withContext(defaultDispatcher) {
        service.addCredit(token, authToken)
    }
}