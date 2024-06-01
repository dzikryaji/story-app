package das.mobile.storyapp.view.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.data.pref.UserModel

class SplashScreenViewModel(private val repository: StoryRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

}