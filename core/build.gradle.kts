plugins {
    kotlin("jvm")
}

group = "com.game.jumper"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
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