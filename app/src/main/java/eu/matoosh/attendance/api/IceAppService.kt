package eu.matoosh.attendance.api

/**
 * Used to connect to the Unsplash API to fetch photos
 *//*

interface IceAppService {

    @POST("rest/ice/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    companion object {
        private const val BASE_URL = "https://matoosh.eu/"

        fun create(): IceAppService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IceAppService::class.java)
        }
    }
}*/
