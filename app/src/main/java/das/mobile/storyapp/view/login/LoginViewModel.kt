package das.mobile.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.data.api.request.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {
    private val apiResult = MutableLiveData<Result<String>>()

    fun login(request: LoginRequest): LiveData<Result<String>> {
        apiResult.value = Result.Loading
        viewModelScope.launch {
            apiResult.value = repository.login(request)
        }
        return apiResult
    }

}