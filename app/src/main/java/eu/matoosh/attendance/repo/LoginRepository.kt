package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.Session
import javax.inject.Inject

class LoginRepository @Inject constructor(private val service: IceAppService) {
    suspend fun login(username: String, password: String): Session {
        val response = service.login(username, password)
        return Session(response.token, response.validity)
    }
}