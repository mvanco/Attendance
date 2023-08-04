package eu.matoosh.attendance.repo

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("token") val token: String,
    @field:SerializedName("validity") val validity: String
)