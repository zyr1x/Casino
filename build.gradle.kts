@file:Suppress("VulnerableLibrariesLocal")

plugins {
    kotlin("jvm") version "2.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
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
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("ru.lewis.core:Core:2.0")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.12")

    compileOnly("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_VERSION}")

    // kotlin
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))
}

object Versions {
    const val ADVENTURE_VERSION = "4.24.0"
}

kotlin {
    jvmToolchain(11)
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
    depend = listOf("Vault", "DecentHolograms", "Core")
}
