plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Firebase Plugin
    id("com.google.firebase.crashlytics") // Firebase Crashlytics (optional)
}

android {
    namespace = "com.example.biomark"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.biomark"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase BoM (Manages Versions Automatically)
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    // Firebase Dependencies (Version Managed by BoM)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-analytics")

    // Google Sign-In (Ensure compatibility with Firebase BoM)
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Google Play Services (Location API)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Firebase ML Kit - Face Recognition
    implementation("com.google.mlkit:face-detection:16.1.5")

    // TensorFlow Lite for MobileFaceNet model (for advanced face recognition)
    implementation("org.tensorflow:tensorflow-lite:2.13.0")

    // AndroidX Lifecycle components (Use KTX for better support)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Biometric Authentication
    implementation("androidx.biometric:biometric:1.1.0")
    implementation(libs.firebase.crashlytics.buildtools)

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.github.bumptech.glide:glide:4.16.0") // Use latest version
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.google.firebase:firebase-database:20.0.5")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.mlkit:face-detection:16.0.5")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.mlkit:face-detection:16.0.5")
    implementation ("org.tensorflow:tensorflow-lite:2.9.0")




}
