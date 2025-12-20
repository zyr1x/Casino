@file:Suppress("VulnerableLibrariesLocal")

plugins {
    kotlin("jvm") version "2.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.gradleup.shadow") version "8.3.3"
}

group = "ru.lewis.casino"
version = "2.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://repo.panda-lang.org/releases")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    // kotlin
    library(kotlin("stdlib"))
    library(kotlin("reflect"))

    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")

    compileOnly(files("gradle/libs/multiverse-core-4.3.16.jar"))

    compileOnly("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_VERSION}")
    compileOnly("net.kyori:adventure-platform-bungeecord:4.4.1")

    library("com.google.inject:guice:5.1.0")
    library("com.google.inject.extensions:guice-assistedinject:5.1.0")

    implementation("xyz.xenondevs.invui:invui-core:1.46")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.46")
    implementation("xyz.xenondevs.invui:inventory-access-r7:1.46")

    implementation("dev.rollczi:litecommands-bukkit:3.10.4")
    implementation("dev.rollczi:litecommands-adventure:3.10.4")

    library("org.spongepowered:configurate-yaml:4.2.0")
    library("org.spongepowered:configurate-extra-kotlin:4.2.0")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.9.9")

    compileOnly("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_VERSION}")

    library("io.github.blackbaroness:duration-serializer:2.0.2")
}

tasks.shadowJar {
    archiveBaseName.set("Casino")
    archiveVersion.set("2.0")

    exclude("kotlin/**")
    exclude("kotlinx/**")

    // Исключаем метаданные и вспомогательные файлы
    exclude("META-INF/maven/org.jetbrains.kotlin/**")
    exclude("**/*.kotlin_metadata")
    exclude("**/*.kotlin_builtins")
    exclude("META-INF/*.kotlin_module")

    relocate("xyz.xenondevs", "ru.lewis.core.__relocated__.invui")
    relocate("dev.rollczi", "ru.lewis.core.__relocated__.litecommands")

    archiveClassifier = ""

    exclude("colors.bin")
}

object Versions {
    const val ADVENTURE_VERSION = "4.24.0"
}

kotlin {
    jvmToolchain(21)
}

tasks.compileKotlin.configure {
    compilerOptions.javaParameters = true
}

bukkit {
    name = "Casino"
    version = "2.0"
    main = "ru.lewis.casino.bootstrap.Bootstrap"
    apiVersion = "1.16"
    author = "Lewis Carrol"
    generateLibrariesJson = true
    depend = listOf("Vault", "DecentHolograms", "Multiverse-Core")
    softDepend = listOf("PlaceHolderAPI")
}
