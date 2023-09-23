package eu.matoosh.attendance.api

import com.google.gson.annotations.SerializedName
import eu.matoosh.attendance.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class ApiLoginResponse(
    @field:SerializedName("token") val token: String?,
    @field:SerializedName("validity") val validity: String?,
    @field:SerializedName("user_id") val userId: Int?,
    @field:SerializedName("error") val error: String?,
)

data class ApiUser(
    @field:SerializedName("credit") val credit: Int?,
    @field:SerializedName("email") val email: String?,
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("username") val username: String?
)

data class ApiBookResponse(
    @field:SerializedName("unchecked_list") val uncheckedList: List<ApiUser>?,
    @field:SerializedName("error") val error: String?,
)

data class ApiCheckResponse(
    @field:SerializedName("order") val order: Int?,
    @field:SerializedName("result") val result: String?,
    @field:SerializedName("error") val error: String?,
)

data class ApiUncheckResponse(
    @field:SerializedName("result") val result: String?,
    @field:SerializedName("error") val error: String?,
)

data class ApiIssueAuthorizationResponse(
    @field:SerializedName("auth_token") val authToken: String,
    @field:SerializedName("result") val result: String
)

data class ApiAddCreditResponse(
    @field:SerializedName("result") val result: String
)

interface IceAppService {
    @FormUrlEncoded
    @POST("rest/ice/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String
    ): ApiLoginResponse

    @FormUrlEncoded
    @POST("rest/ice/unchecked_list")
    suspend fun uncheckedList(
        @Field("token") token: String
    ): ApiBookResponse

    @FormUrlEncoded
    @POST("rest/ice/check")
    suspend fun check(
        @Field("token") token: String,
        @Field("user_id") userId: String
    ): ApiCheckResponse

    @FormUrlEncoded
    @POST("rest/ice/uncheck")
    suspend fun uncheck(
        @Field("token") token: String
    ): ApiUncheckResponse

    @FormUrlEncoded
    @POST("rest/ice/issue_auth")
    suspend fun issueAuthorization(
        @Field("token") token: String,
        @Field("credit") credit: String
    ): ApiIssueAuthorizationResponse

    @FormUrlEncoded
    @POST("rest/ice/add_credit")
    suspend fun addCredit(
        @Field("token") token: String,
        @Field("auth_token") authToken: String
    ): ApiAddCreditResponse

    companion object {
        fun create(): IceAppService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IceAppService::class.java)
        }
    }
}
