package ru.lewis.casino.bootstrap

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.onarandombox.MultiverseCore.MultiverseCore
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.casino.model.AssistedInjectFactories
import java.io.File

class InjectionModule(
    private val plugin: Plugin,
) : AbstractModule() {

    override fun configure() {
        install(
            FactoryModuleBuilder()
                .build(AssistedInjectFactories::class.java)
        )
    }

    @Provides
    fun provideMultiVerseCore(): MultiverseCore? = Bukkit.getPluginManager().getPlugin("Multiverse-Core") as? MultiverseCore

    @Provides
    fun provideCasesPlugin(): Plugin = plugin

    @Provides
    fun MiniMessage(): MiniMessage = MiniMessage.miniMessage()

    @Provides
    fun Server(plugin: Plugin): Server = plugin.server

    @Provides
    fun Logger(plugin: Plugin): Logger = plugin.slF4JLogger

    @Provides
    fun dataFolder(plugin: Plugin): File = plugin.dataFolder
}
