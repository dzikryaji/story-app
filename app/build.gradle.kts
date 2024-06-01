plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "das.mobile.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "das.mobile.storyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL" ,"\"https://story-api.dicoding.dev/v1/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit) //Retrofit
    implementation(libs.converter.gson) //Gson Converter
    implementation(libs.logging.interceptor) // Logging Interceptor
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle Runtime
    implementation(libs.androidx.datastore.preferences) // Data Store
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Lifecycle ViewModel
    implementation(libs.androidx.lifecycle.livedata.ktx) // Lifecycle LiveData
    implementation(libs.androidx.activity.ktx) // Activity Ktx
    implementation(libs.glide) // Glide
    implementation(libs.play.services.maps) // Maps
    implementation(libs.play.services.location) // Location
    implementation(libs.androidx.room.ktx) // Room
    ksp(libs.androidx.room.compiler) // Room Compiler
    implementation(libs.androidx.room.paging) // Room Paging
    implementation(libs.androidx.paging.runtime.ktx) // Paging Runtime

    //Testing
    androidTestImplementation(libs.androidx.core.testing) //InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test) //TestDispatcher

    testImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) //TestDispatcher
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
}
