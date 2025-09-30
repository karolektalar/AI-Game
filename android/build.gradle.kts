plugins {
    id("com.android.application")
    kotlin("android")
}

configurations {
    create("natives")
}

android {
    namespace = "com.game.jumper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.game.jumper"
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

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        named("main") {
            java.srcDirs("src/main/java")
            jniLibs.srcDirs("libs")
        }
    }
}

// Task to copy native libraries
tasks.register("copyAndroidNatives") {
    doFirst {
        val natives = configurations.getByName("natives")

        natives.files.forEach { jar ->
            val jarName = jar.nameWithoutExtension
            // Extract ABI from jar name (e.g., "gdx-platform-1.12.1-natives-arm64-v8a" -> "arm64-v8a")
            val abi = when {
                jarName.contains("arm64-v8a") -> "arm64-v8a"
                jarName.contains("armeabi-v7a") -> "armeabi-v7a"
                jarName.contains("x86_64") -> "x86_64"
                jarName.contains("x86") -> "x86"
                else -> null
            }

            if (abi != null) {
                val outputDir = file("libs/$abi")
                outputDir.mkdirs()

                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.whenTaskAdded {
    if (name == "preBuild" || name == "packageDebug" || name == "packageRelease") {
        dependsOn("copyAndroidNatives")
    }
}

dependencies {
    implementation(project(":core"))

    // LibGDX dependencies
    api("com.badlogicgames.gdx:gdx:1.12.1")
    api("com.badlogicgames.gdx:gdx-backend-android:1.12.1")

    // Native libraries - using 'natives' configuration for proper packaging
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-armeabi-v7a")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-arm64-v8a")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-x86")
    "natives"("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-x86_64")

    // AdMob
    implementation("com.google.android.gms:play-services-ads:22.6.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
}

// Dependency resolution strategy to ensure native libs are extracted
configurations.all {
    resolutionStrategy {
        force("com.badlogicgames.gdx:gdx-platform:1.12.1")
    }
}