package das.mobile.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.data.api.response.Story
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    private val storyResult = MutableLiveData<Result<List<Story>>>()
    fun getAllStoryWithLocation(): LiveData<Result<List<Story>>> {
        storyResult.value = Result.Loading
        viewModelScope.launch {
            storyResult.value = repository.getStoriesWithLocation()
        }
        return storyResult
    }
}