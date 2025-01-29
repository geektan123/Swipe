plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Retrofit for API calls
        implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ViewModel & LiveData (Jetpack)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("androidx.work:work-runtime:2.7.1") // or the latest version

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.activity)
    implementation(libs.play.services.cast.framework)
    implementation(libs.androidx.work.runtime)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Material Design Components
    implementation("com.google.android.material:material:1.9.0")

    // Android Core Libraries
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

     implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
       implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    //
    //    // RecyclerView
      implementation("androidx.recyclerview:recyclerview:1.3.2")
    //
    //    // AppCompat for backward compatibility
       implementation("androidx.appcompat:appcompat:1.6.1")
    //
    //    // Material Design Components
     implementation("com.google.android.material:material:1.10.0")
    //
    //    // Retrofit for API calls
       implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //
    //    // Glide for image loading
      implementation("com.github.bumptech.glide:glide:4.15.1")
    //
    //    // ImagePicker for image selection
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Jetpack Compose Dependencies
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
        val room_version = "2.6.1"

        implementation("androidx.room:room-runtime:$room_version")

        // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
        // See Add the KSP plugin to your project

        // If this project only uses Java source, use the Java annotationProcessor
        // No additional plugins are necessary
        annotationProcessor("androidx.room:room-compiler:$room_version")

        // optional - Kotlin Extensions and Coroutines support for Room
        implementation("androidx.room:room-ktx:$room_version")

        // optional - RxJava2 support for Room
        implementation("androidx.room:room-rxjava2:$room_version")

        // optional - RxJava3 support for Room
        implementation("androidx.room:room-rxjava3:$room_version")

        // optional - Guava support for Room, including Optional and ListenableFuture
        implementation("androidx.room:room-guava:$room_version")

        // optional - Test helpers
        testImplementation("androidx.room:room-testing:$room_version")

        // optional - Paging 3 Integration
        implementation("androidx.room:room-paging:$room_version")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
