package das.mobile.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.di.Injection
import das.mobile.storyapp.view.addstory.AddStoryViewModel
import das.mobile.storyapp.view.login.LoginViewModel
import das.mobile.storyapp.view.main.MainViewModel
import das.mobile.storyapp.view.maps.MapsViewModel
import das.mobile.storyapp.view.register.RegisterViewModel
import das.mobile.storyapp.view.splashscreen.SplashScreenViewModel

class ViewModelFactory(
    private val repository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) -> {
                SplashScreenViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(MainViewModel::class.java)) -> {
                MainViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(LoginViewModel::class.java)) -> {
                LoginViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(RegisterViewModel::class.java)) -> {
                RegisterViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) -> {
                AddStoryViewModel(repository) as T
            }
            (modelClass.isAssignableFrom(MapsViewModel::class.java)) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }

        fun deleteInstance(){
            INSTANCE = null
        }
    }
}