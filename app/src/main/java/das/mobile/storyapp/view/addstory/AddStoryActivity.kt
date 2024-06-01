package das.mobile.storyapp.view.addstory

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.databinding.ActivityAddStoryBinding
import das.mobile.storyapp.utils.getImageUri
import das.mobile.storyapp.view.ViewModelFactory
import java.util.concurrent.TimeUnit

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var cameraUri: Uri? = null

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()
        createLocationCallback()
        setupView()
    }

    private fun setupView() {
        binding.apply {

            viewModel.getUri().observe(this@AddStoryActivity) {
                showImage(it)
            }

            edAddDescription.addTextChangedListener {
                viewModel.setDescription(edAddDescription.text.toString())
            }

            viewModel.getDescription().observe(this@AddStoryActivity) {
                if (edAddDescription.text.toString() != it) {
                    edAddDescription.setText(it)
                }
            }

            viewModel.getResult().observe(this@AddStoryActivity) { result ->
                when (result) {
                    is Result.Loading -> progressIndicator.visibility = View.VISIBLE
                    is Result.Error -> {
                        progressIndicator.visibility = View.GONE
                        Toast.makeText(this@AddStoryActivity, result.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Result.Success -> {
                        Toast.makeText(applicationContext, "Upload Success", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            }

            galleryButton.setOnClickListener { startGallery() }
            cameraButton.setOnClickListener { startCamera() }

            uploadButton.setOnClickListener {
                if (cbLocation.isChecked) {
                    viewModel.getLocation().observe(this@AddStoryActivity){ location ->
                        if (location == null){
                            progressIndicator.visibility = View.VISIBLE
                        } else {
                            stopLocationUpdates()
                            viewModel.uploadImage(this@AddStoryActivity, true)
                        }
                    }
                    } else {
                    viewModel.uploadImage(this@AddStoryActivity)
                }
            }

            cbLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkPermissionAndGPS()
                } else {
                    stopLocationUpdates()
                }
            }
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setUri(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }

    }

    private fun startCamera() {
        cameraUri = getImageUri(this)
        cameraUri?.let { launcherIntentCamera.launch(it) }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            cameraUri?.let { viewModel.setUri(it) }
        }
    }

    private fun showImage(uri: Uri?) {
        uri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            checkPermissionAndGPS()
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionAndGPS() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val client = LocationServices.getSettingsClient(this)
            client.checkLocationSettings(builder.build())
                .addOnSuccessListener { startLocationUpdates() }
                .addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        try {
                            resolutionLauncher.launch(
                                IntentSenderRequest.Builder(exception.resolution).build()
                            )
                        } catch (sendEx: IntentSender.SendIntentException) {
                            Toast.makeText(
                                this@AddStoryActivity,
                                sendEx.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        } else {
            Log.d(TAG, "getMyLastLocation: Didn't get permission")
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
                    startLocationUpdates()
                }

                RESULT_CANCELED ->
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, // Priority
            TimeUnit.SECONDS.toMillis(1) // Interval
        ).apply {
            setWaitForAccurateLocation(false)
            setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(1)) // Min update interval
            setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(1)) // Max wait time
        }.build()
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
                    val lastLatLng = LatLng(location.latitude, location.longitude)

                    viewModel.setLocation(lastLatLng)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val TAG = "AddStoryActivity"
    }
}