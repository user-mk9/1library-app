plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.mubarakalabi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // dependencies for logging
    implementation("io.github.microutils:kotlin-logging:2.1.23")
    implementation("org.slf4j:slf4j-simple:1.7.36")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}