package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.Device
import eu.matoosh.attendance.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface RepoLoginResponse {
    data class Success(val token: String, val validity: String, val userId: Int) : RepoLoginResponse
    data class Error(val message: String, val errorCode: RepoLoginErrorCode) : RepoLoginResponse
}

enum class RepoLoginErrorCode(val serverField: String?) {
    INCOMPATIBILITTY("incompatibitlity"),
    INCORRECT_USERNAME("incorrect-username"),
    INCORRECT_PASSWORD("incorrect-password"),
    UNKNOWN(null)
}

class LoginRepository @Inject constructor(
    private val service: IceAppService,
    private val device: Device,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun login(username: String, password: String): RepoLoginResponse = withContext(defaultDispatcher) {
        val deviceId = if (username == "admin") {
            "Not protected"
        }
        else {
            device.androidId
        }
        val response = service.login(username, password, deviceId)
        if (response.error == null) {
            if (response.token != null && response.validity != null && response.userId != null) {
                RepoLoginResponse.Success(response.token, response.validity, response.userId)
            } else {
                RepoLoginResponse.Error("Incompatible API version!",
                    RepoLoginErrorCode.INCOMPATIBILITTY
                )
            }
        } else {
            val errorCode = RepoLoginErrorCode.values().find { it.serverField == response.errorCode }
            RepoLoginResponse.Error(response.error, errorCode ?: RepoLoginErrorCode.UNKNOWN)
        }
    }
}