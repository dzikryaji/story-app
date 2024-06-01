package das.mobile.storyapp.data.api

import das.mobile.storyapp.data.api.request.LoginRequest
import das.mobile.storyapp.data.api.request.RegisterRequest
import das.mobile.storyapp.data.api.response.ApiResponse
import das.mobile.storyapp.data.api.response.LoginResponse
import das.mobile.storyapp.data.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): ApiResponse

    @POST("login")
    suspend fun login(
        @Body response: LoginRequest
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ): ApiResponse

    @GET("stories")
    suspend fun getStories(
        @Query("location") location: Int = 0,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): StoryResponse
}