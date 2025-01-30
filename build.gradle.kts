@file:Suppress("VulnerableLibrariesLocal")

import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    kotlin("jvm") version "2.0.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://repo.panda-lang.org/releases")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    val guiceVersion = "7.0.0"
    val invUiVersion = "1.32"
    val configurateVersion = "4.1.2"
    val adventureVersion = "4.3.3"

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("me.lucko:helper:5.6.14")
    compileOnly ("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.12")

    // kotlin
    library(kotlin("stdlib"))
    library(kotlin("reflect"))

    // guice
    library("com.google.inject:guice:$guiceVersion")
    library("com.google.inject.extensions:guice-assistedinject:$guiceVersion")

    // invui
    library("xyz.xenondevs.invui:invui-core:$invUiVersion")
    library("xyz.xenondevs.invui:invui-kotlin:$invUiVersion")
    (18..20).forEach { library("xyz.xenondevs.invui:inventory-access-r$it:$invUiVersion") }

    // configurate
    library("org.spongepowered:configurate-yaml:$configurateVersion")
    library("org.spongepowered:configurate-extra-kotlin:$configurateVersion")

    // adventure
    library("net.kyori:adventure-platform-bukkit:$adventureVersion")
    library("net.kyori:adventure-text-serializer-bungeecord:$adventureVersion")
    library("net.kyori:adventure-text-minimessage:4.17.0")

    // misc
    library("it.unimi.dsi:fastutil:8.5.13")
    library("io.github.blackbaroness:fastutil-extender-common:1.2.0")
    library("dev.rollczi:litecommands-bukkit:3.4.2")
    library("io.github.blackbaroness:duration-serializer:2.0.2")
    library("commons-io:commons-io:2.16.1")
    library("org.apache.commons:commons-lang3:3.14.0")
    library("de.tr7zw:item-nbt-api:2.13.1")
    library("com.fasterxml.jackson.core:jackson-databind:2.17.1")

}

kotlin {
    jvmToolchain(21)
}

tasks.compileKotlin.configure {
    compilerOptions.javaParameters = true
}

paper {
    version = "1.1 STABLE"
    main = "ru.lewis.casino.bootstrap.Bootstrap"
    loader = "ru.lewis.casino.bootstrap.Loader"
    apiVersion = "1.20"
    author = "Lewis Carrol"
    generateLibrariesJson = true
    hasOpenClassloader = false

    serverDependencies {
        register("helper") { load = PaperPluginDescription.RelativeLoadOrder.BEFORE }
        register("LuckPerms") { load = PaperPluginDescription.RelativeLoadOrder.BEFORE }
        register("PlaceholderAPI") { load = PaperPluginDescription.RelativeLoadOrder.BEFORE }
        register("Vault") { load = PaperPluginDescription.RelativeLoadOrder.BEFORE }
        register("DecentHolograms") { load = PaperPluginDescription.RelativeLoadOrder.BEFORE }
    }
}
