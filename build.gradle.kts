plugins {
    id("java")
}

group = "yuuria"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

}

idea {
    module {
        downloadSources = true
        downloadJavadoc = true
    }
}