
rootProject.name = "BlockDisplayer"
include("BlockDisplayerBukkit")
include("BlockDisplayerFabric")

pluginManagement {
    repositories {
        jcenter()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }

    plugins {
        id("fabric-loom") version "1.1-SNAPSHOT"
        id("org.jetbrains.kotlin.jvm") version "1.7.10"
    }

}
