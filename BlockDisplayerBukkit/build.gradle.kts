dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("io.github.monun:kommand-api:3.1.6")
    implementation("com.sk89q.worldedit:worldedit-bukkit:${properties["worldedit_api_version"]}")
}

tasks.withType<ProcessResources> {
    inputs.property("version", project.version)

    filesMatching("plugin.yml") {
        expand(mapOf(Pair("version", project.version)))
    }
}