plugins {
    kotlin("jvm") version "1.8.21"
}

allprojects {
    group = "kr.icetang0123"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://maven.enginehub.org/repo/") }
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }
}