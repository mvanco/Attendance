package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.Device
import eu.matoosh.attendance.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface RepoLoginResponse {
    data class Success(val token: String, val validity: String) : RepoLoginResponse
    data class Error(val message: String) : RepoLoginResponse
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