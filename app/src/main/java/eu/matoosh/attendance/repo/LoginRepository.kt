package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.Device
import javax.inject.Inject

sealed interface RepoLoginResponse {
    data class Success(val token: String, val validity: String) : RepoLoginResponse
    data class Error(val message: String) : RepoLoginResponse
}

class LoginRepository @Inject constructor(
    private val service: IceAppService,
    private val device: Device
) {
    suspend fun login(username: String, password: String): RepoLoginResponse {
        val deviceId = if (username == "admin") {
            "Not protected"
        }
        else {
            device.androidId
        }
        val response = service.login(username, password, deviceId)
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