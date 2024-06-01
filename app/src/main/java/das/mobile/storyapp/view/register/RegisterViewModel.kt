package das.mobile.storyapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.data.api.request.RegisterRequest
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: StoryRepository) : ViewModel() {
    private val apiResult = MutableLiveData<Result<String>>()
    fun register(request: RegisterRequest): LiveData<Result<String>> {
        apiResult.value = Result.Loading
        viewModelScope.launch {
            apiResult.value = repository.register(request)
            Log.d(TAG, "register: ${apiResult.value}")
        }
        return apiResult
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}