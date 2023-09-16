plugins {
    kotlin("jvm") version "1.8.21"
    `maven-publish`
}

allprojects {
    group = "xyz.icetang0123"
    version = "1.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://maven.enginehub.org/repo/") }
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("maven-publish")
    }
}