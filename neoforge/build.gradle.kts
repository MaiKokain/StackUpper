plugins {
    id("java")
    id("net.neoforged.moddev") version "2.0.141"
    id("idea")
}

repositories {
    mavenLocal()
    mavenCentral()
}


neoForge {
    version = property("deps.neoforge") as String
    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
    }
}
base.archivesName = "${property("mod.name")}-${property("mod.version")}-${property("deps.mcVersion")}"

dependencies {
    implementation(group = "yuuria.stackupper", name = "StackUpperConfigLibrary", version = "[1,)")
    jarJar(implementation(group = "yuuria.stackupper", name = "StackUpperConfigLibrary", version = "[1,)"))
    jarJar("org.antlr:antlr4-runtime:4.13.2")
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(tasks.named("stonecutterGenerate"))

    val props = mapOf(
        "version" to prop("mod.version"),
        "minecraft" to prop("deps.mcVersion"),
        "id" to prop("mod.id"),
        "name" to prop("mod.name"),
        "homepage_url" to prop("mod.homepage_url"),
        "authors" to prop("mod.authors"),
        "description" to prop("mod.description"),
        "issue_url" to prop("mod.issue_url"),
        "license" to prop("mod.license")
    )
    expand(props)
    from(rootProject.file("src/main/resources/META-INF/neoforge.mods.toml"))
    into(project.file("src/main/resources/META-INF/neoforge.mods.toml"))
}

fun prop(name: String): String {
    return (findProperty("${stonecutter.current.version}.$name") ?: property(name)) as String
}