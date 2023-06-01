val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.0"

    id("info.solidsoft.pitest") version "1.9.11"
    kotlin("plugin.serialization") version "1.8.20"
}

group = "io.qmppu842"
version = "0.0.1"
application {
    mainClass.set("io.qmppu842.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")


    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
}

val jvmTargetCompatibility = "1.8"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = jvmTargetCompatibility
    }
}
tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = jvmTargetCompatibility
    targetCompatibility = jvmTargetCompatibility
}


tasks.test {
    useJUnitPlatform()
}