package das.mobile.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.data.api.response.Story
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStories(): LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)
    fun logout() = viewModelScope.launch {
        repository.logout()
        onCleared()
    }

}