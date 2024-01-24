package eu.matoosh.attendance.seznam.api

import com.google.gson.annotations.SerializedName
import eu.matoosh.attendance.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class ApiErrorResponse(
    @field:SerializedName("code") val code: Int?,
    @field:SerializedName("message") val message: String?
)

data class ApiImageLinksResponse(
    @field:SerializedName("thumbnail") val thumbnail: String?,
    @field:SerializedName("medium") val medium: String?
)

data class ApiAccessInfo(
    @field:SerializedName("webReaderLink") val webReaderLink: String?
)

data class ApiVolumeInfoResponse(
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("authors") val authors: List<String>?,
    @field:SerializedName("publishedDate") val publishedDate: String?,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("imageLinks") val imageLinks: ApiImageLinksResponse?,
    @field:SerializedName("language") val language: String?,
    @field:SerializedName("infoLink") val infoLink: String?,
)

data class ApiBookDetailResponse(
    @field:SerializedName("id") val id: String?,
    @field:SerializedName("volumeInfo") val volumeInfo: ApiVolumeInfoResponse?,
    @field:SerializedName("accessInfo") val accessInfo: ApiAccessInfo?,
    @field:SerializedName("error") val error: ApiErrorResponse?
)

data class ApiBooksResponse(
    @field:SerializedName("kind") val token: String?,
    @field:SerializedName("totalItems") val totalItems: Int?,
    @field:SerializedName("items") val items: List<ApiBookDetailResponse>?,
    @field:SerializedName("error") val error: ApiErrorResponse?
)

interface SeznamService {
    @GET("books/v1/volumes")
    suspend fun getBooks(
        @Query("q") q: String
    ): ApiBooksResponse

    @GET("books/v1/volumes/{id}")
    suspend fun getBookDetail(
        @Path("id") id: String
    ): ApiBookDetailResponse


    companion object {
        fun create(): SeznamService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SeznamService::class.java)
        }
    }
}