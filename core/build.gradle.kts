plugins {
    kotlin("jvm")
}

group = "com.game.jumper"
version = "1.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:1.12.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
}