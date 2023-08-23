package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.ApiAddCreditResponse
import eu.matoosh.attendance.api.ApiIssueAuthorizationResponse
import eu.matoosh.attendance.api.IceAppService
import javax.inject.Inject

class CreditRepository @Inject constructor(
    private val service: IceAppService
) {
    suspend fun issueAuthorization(token: String, credit: Int): ApiIssueAuthorizationResponse {
        return service.issueAuthorization(token, credit.toString())
    }

    suspend fun addCredit(token: String, authToken: String): ApiAddCreditResponse {
        return service.addCredit(token, authToken)
    }
}