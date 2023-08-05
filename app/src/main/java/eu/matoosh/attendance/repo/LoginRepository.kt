package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import javax.inject.Inject

sealed interface RepoLoginResponse {
    data class Success(val token: String, val validity: String) : RepoLoginResponse
    data class Error(val message: String) : RepoLoginResponse
}

class LoginRepository @Inject constructor(private val service: IceAppService) {
    suspend fun login(username: String, password: String): RepoLoginResponse {
        val response = service.login(username, password)
        return if (response.error == null) {
            if (response.token != null && response.validity != null) {
                RepoLoginResponse.Success(response.token, response.validity)
            } else {
                RepoLoginResponse.Error("Incompatible API version!")
            }
        } else {
            RepoLoginResponse.Error(response.error)
        }
    }
}