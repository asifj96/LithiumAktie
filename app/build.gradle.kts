plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinExtensions)
    id(Plugins.googleServices)
    id(Plugins.firebaseCrashlytics)
    id(Plugins.navigationSafeArgs)
    id(Plugins.firebasePerformance)
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.Arbor.lithiumaktie"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = Versions.jvmTarget
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Android
    implementation(Android.coreKtx)
    implementation(Android.appCompat)
    implementation(Android.materialDesign)
    implementation(Android.constraintLayout)
    implementation(Android.multiDex)
    implementation(Android.sdp)
    implementation(Android.ssp)
    implementation(Android.recyclerView)
    implementation(Android.lottieAnim)
    implementation(Android.fragmentKtx)
    implementation(Android.activityKtx)
    implementation(Android.workMangerKtx)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
//    kapt(Android.dataBindingCompiler)
    testImplementation(Android.junit)
    androidTestImplementation(Android.junitTest)
    androidTestImplementation(Android.espressoCore)

    // Firebase
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.firebaseCrashlytics)
    implementation(Firebase.firebaseAnalytics)
    implementation(Firebase.firebaseConfig)
    implementation(Firebase.firebaseInstallations)
    implementation(Firebase.firebasePerformance)

    // Glide
    implementation(Glide.glide)
    annotationProcessor(Glide.glideCompiler)

    // Navigation
    implementation(Navigation.fragmentKtx)
    implementation(Navigation.uiKtx)

    // Timber
    api("com.jakewharton.timber:timber:4.7.1")

    // LifeCycle
    implementation(LifeCycle.viewmodelKtx)
    implementation(LifeCycle.livedataKtx)
    implementation(LifeCycle.lifecycleExtensions)
    implementation(LifeCycle.runtimeKtx)

    implementation("io.github.glailton.expandabletextview:expandabletextview:1.0.2")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation(Gson.gson)

    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

}
kapt {
    correctErrorTypes = true
}