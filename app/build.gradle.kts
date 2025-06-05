plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.classy.poolschedule"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.classy.poolschedule"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true


    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.recyclerview)
    implementation (libs.cardview)
    implementation (libs.lifecycle.extensions)
    implementation (libs.lifecycle.viewmodel)
    implementation (libs.firebase.firestore)
    implementation (libs.firebase.auth)

    coreLibraryDesugaring (libs.desugar.jdk.libs)
    implementation (libs.play.services.tasks)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation (libs.material.v1110)
    implementation (libs.gson)
}