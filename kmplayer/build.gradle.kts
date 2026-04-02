import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "io.github.msusman.kmplayer"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KMPlayer"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // No external deps for the core player API/state machine.
        }
        androidMain.dependencies {
            implementation(libs.androidx.media3.exoplayer)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
