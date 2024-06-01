package das.mobile.storyapp.di

import android.content.Context
import das.mobile.storyapp.data.StoryRepository
import das.mobile.storyapp.data.api.ApiConfig
import das.mobile.storyapp.data.local.StoryDatabase
import das.mobile.storyapp.data.pref.UserPreference
import das.mobile.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, database, pref)
    }
}