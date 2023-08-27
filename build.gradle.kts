plugins {
    kotlin("jvm") version "1.8.21"
    `maven-publish`
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
        plugin("maven-publish")
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
            }
        }
    }
}