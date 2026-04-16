rootProject.name = "stackupper"
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val neoforgeVersions = listOf<String>("26.1")

stonecutter {
    create(rootProject) {
        branch("neoforge") {
            neoforgeVersions.forEach { s -> version("neoforge-${s}", s) }
        }
    }
}