plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.kmplayer.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kmplayer.app"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    implementation(projects.sampleApp.shared)
//    implementation(libs.compose.components.uiToolingPreview)
    implementation(libs.androidx.activity.compose)
}
