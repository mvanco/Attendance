package eu.matoosh.attendance.repo

import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.data.SessionManager
import eu.matoosh.attendance.data.User
import javax.inject.Inject


sealed interface RepoBookResponse {
    data class Success(val users: List<User>) : RepoBookResponse
    data class Error(val message: String) : RepoBookResponse
}

sealed interface RepoCheckResponse {
    data class Success(val order: Int) : RepoCheckResponse
    data class Error(val message: String) : RepoCheckResponse
}

sealed interface RepoUncheckResponse {
    object Success : RepoUncheckResponse
    data class Error(val message: String) : RepoUncheckResponse
}

class BookRepository @Inject constructor(
    private val service: IceAppService
) {
    suspend fun uncheckedList(token: String): RepoBookResponse {
        val response = service.uncheckedList(token)
        return if (response.error == null) {
            if (response.uncheckedList != null) {
                RepoBookResponse.Success(response.uncheckedList.map {
                    if (it.id != null && it.username != null && it.email != null && it.credit != null) {
                        User(it.id, it.username, it.email, it.credit)
                    }
                    else {
                        return RepoBookResponse.Error("Incompatible API version!")
                    }
                })
            } else {
                RepoBookResponse.Error("Incompatible API version!")
            }
        } else {
            RepoBookResponse.Error(response.error)
        }
    }


    suspend fun check(token: String, user: User): RepoCheckResponse {
        val response = service.check(token, user.id.toString())
        return if (response.error == null) {
            if (response.result != null && response.result == "Success" && response.order != null) {
                RepoCheckResponse.Success(response.order)
            } else {
                RepoCheckResponse.Error("Incompatible API version!")
            }
        } else {
            RepoCheckResponse.Error(response.error)
        }
    }
}