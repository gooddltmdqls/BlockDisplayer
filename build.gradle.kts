plugins {
    kotlin("jvm") version "1.8.21"
}

group = "kr.icetang0123"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("io.github.monun:kommand-api:3.1.6")
    implementation("com.sk89q.worldedit:worldedit-bukkit:7.2.15")
}