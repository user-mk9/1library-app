import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20-RC"
    // Plugin for Dokka - KDoc generating tool
    id("org.jetbrains.dokka") version "1.6.10"
    jacoco
    // Plugin for Ktlint
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    // Plugin for version checker
    id("org.sirekanyan.version-checker") version "1.0.2"
    application
}

group = "me.mubarakalabi"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // dependencies for logging
    implementation("io.github.microutils:kotlin-logging:2.1.23")
    implementation("org.slf4j:slf4j-simple:1.7.36")

    // For Streaming to XML and JSON
    implementation("com.thoughtworks.xstream:xstream:1.4.18")
    implementation("org.codehaus.jettison:jettison:1.4.1")

    // For generating a Dokka Site from KDoc
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")

    // CBOR Gradle
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.5.0")

    // YAML
    implementation("org.yaml:snakeyaml:1.28")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
    // report is always generated after tests run
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jar {
    manifest.attributes["Main-Class"] = "MainKt"
    // for building a fat jar - include all dependencies
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

// This plugin checks the versions of the specified dependencies against the constraints specified in the
// `versionCheckerOptions` block and generates a report of any dependencies that do not meet the constraints.
versionCheckerOptions {
    "com.squareup.okhttp3:logging-interceptor" lessThan "4.0"
}

application {
    mainClass.set("MainKt")
}
