package das.mobile.storyapp.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import das.mobile.storyapp.data.api.ApiService
import das.mobile.storyapp.data.api.request.LoginRequest
import das.mobile.storyapp.data.api.request.RegisterRequest
import das.mobile.storyapp.data.api.response.ApiResponse
import das.mobile.storyapp.data.api.response.Story
import das.mobile.storyapp.data.local.StoryDatabase
import das.mobile.storyapp.data.pref.UserModel
import das.mobile.storyapp.data.pref.UserPreference
import das.mobile.storyapp.utils.reduceFileImage
import das.mobile.storyapp.utils.uriToFile
import das.mobile.storyapp.view.ViewModelFactory
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val database: StoryDatabase,
    private val userPreference: UserPreference
) {
    suspend fun register(request: RegisterRequest): Result<String> {
        var apiResult: Result<String>
        try {
            val successResponse = apiService.register(request)
            Log.d(TAG, "register: $successResponse")
            Log.d(TAG, "register: ${successResponse.message}")
            apiResult= Result.Success(successResponse.message)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
            apiResult = Result.Error(errorResponse.message)
            Log.d(TAG, "register error: $errorResponse")
            Log.d(TAG, "register error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "register timeout: ${e.message}")
            apiResult = Result.Error("Socket Timeout")
        }
        return apiResult
    }

    suspend fun login(request: LoginRequest): Result<String> {
        var apiResult: Result<String>
        try {
            val successResponse = apiService.login(request)
            Log.d(TAG, "login: $successResponse")
            Log.d(TAG, "login: ${successResponse.message}")
            val user = UserModel(request.email, successResponse.loginResult.token, true)
            userPreference.saveSession(user)
            apiResult = Result.Success(successResponse.message)
            INSTANCE = null
            ViewModelFactory.deleteInstance()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
            apiResult = Result.Error(errorResponse.message)
            Log.d(TAG, "login error: $errorResponse")
            Log.d(TAG, "login error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login timeout: ${e.message}")
            apiResult = Result.Error("Socket Timeout")
        }
        return apiResult
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getStories()
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(): Result<List<Story>> {
        val storyResult: Result<List<Story>> = try {
            val storyResponse = apiService.getStories(location = 1)
            Log.d(TAG, "get stories: ${storyResponse.message}")
            Result.Success(storyResponse.listStory)
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
            Log.d(TAG, "get stories error: $errorResponse")
            Log.d(TAG, "get stories error: ${errorResponse.message}")
            Result.Error(errorResponse.message)
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login timeout: ${e.message}")
            Result.Error("Socket Timeout")
        }
        return storyResult
    }

    suspend fun uploadStory(
        context: Context,
        uri: Uri,
        description: String,
        location: LatLng? = null
    ): Result<String> {
        val imageFile = uriToFile(uri, context).reduceFileImage()
        Log.d("Image File", "showImage: ${imageFile.path}")

        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        var requestLat: RequestBody? = null
        var requestLon: RequestBody? = null
        location?.let {
            requestLat = location.latitude.toString().toRequestBody("text/plan".toMediaType())
            requestLon = location.longitude.toString().toRequestBody("text/plan".toMediaType())
        }

        var apiResult: Result<String>
        try {
            val successResponse =
                apiService.uploadStory(multipartBody, requestDescription, requestLat, requestLon)

            apiResult = Result.Success(successResponse.message)
            Log.d(TAG, "uploadImage success: ${successResponse.message}")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
            apiResult = Result.Error(errorResponse.message)
            Log.d(TAG, "uploadImage error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "uploadImage timeout: ${e.message}")
            apiResult = Result.Error("Socket Timeout")
        }

        return apiResult
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        INSTANCE = null
        ViewModelFactory.deleteInstance()
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null
        private const val TAG = "StoryRepository"
        fun getInstance(
            apiService: ApiService,
            database: StoryDatabase,
            userPreference: UserPreference
        ): StoryRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(apiService, database, userPreference)
            }.also { INSTANCE = it }
    }
}