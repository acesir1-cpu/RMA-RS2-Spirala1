plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ba.etf.weatherwatch"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "ba.etf.weatherwatch"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Ukloni stari hamcrest-core:1.3 (dolazi tranzitivno preko junit-a) da ne dođe
// do sukoba s hamcrest:2.2 koji testovi koriste (CoreMatchers.allOf).
configurations.configureEach {
    exclude(group = "org.hamcrest", module = "hamcrest-core")
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
    // Potrebno za profesorske testove (WeatherWatchSpirala1Testovi)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.7.0")  // RecyclerViewActions
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.7.0")  // Intents, IntentMatchers
    androidTestImplementation("androidx.test:core-ktx:1.7.0")                   // ActivityScenario, ApplicationProvider
    androidTestImplementation("androidx.test:rules:1.7.0")
    androidTestImplementation("org.hamcrest:hamcrest:2.2")                       // allOf(Matcher, Matcher)
    testImplementation("org.hamcrest:hamcrest:2.2")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.cardview:cardview:1.0.0")
}