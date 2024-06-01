package das.mobile.storyapp.view.addstory

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import das.mobile.storyapp.R
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.data.StoryRepository
import kotlinx.coroutines.launch

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    private val apiResult = MutableLiveData<Result<String>>()
    private val currentUri = MutableLiveData<Uri>()
    private val description = MutableLiveData<String>()
    private val lastLocation = MutableLiveData<LatLng>()

    fun uploadImage(context: Context, isWithLocation: Boolean = false) {
        apiResult.value = Result.Loading
        currentUri.value?.let { uri ->
            description.value?.let { description ->
                viewModelScope.launch {
                    if (isWithLocation) {
                        lastLocation.value?.let { lastLocation ->
                            apiResult.value = repository.uploadStory(context, uri, description, lastLocation)
                        } ?: run {
                            val msg = context.getString(R.string.something_went_wrong)
                            apiResult.value = Result.Error(msg)
                        }
                    } else {
                        apiResult.value = repository.uploadStory(context, uri, description)
                    }
                }
            } ?: run {
                val msg = context.getString(R.string.fill_description)
                apiResult.value = Result.Error(msg)
            }
        } ?: run {
            val msg = context.getString(R.string.choose_image)
            apiResult.value = Result.Error(msg)
        }
    }

    fun getResult(): LiveData<Result<String>> = apiResult

    fun setUri(uri: Uri) {
        currentUri.value = uri
    }

    fun getUri(): LiveData<Uri> = currentUri

    fun setDescription(text: String) {
        description.value = text
    }

    fun getDescription(): LiveData<String> = description

    fun setLocation(location: LatLng) {
        lastLocation.value = location
    }

    fun getLocation(): LiveData<LatLng> = lastLocation

}