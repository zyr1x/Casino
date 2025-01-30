package ru.lewis.casino.bootstrap

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import jakarta.inject.Singleton
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.casino.model.AssistedInjectFactories

class InjectionModule(
    private val plugin: Plugin,
) : AbstractModule() {

    override fun configure() {
        bind(Plugin::class.java).toInstance(plugin)

        install(
            FactoryModuleBuilder()
                .build(AssistedInjectFactories::class.java)
        )
    }

    @Singleton
    @Provides
    fun BukkitAudiences(plugin: Plugin): BukkitAudiences = BukkitAudiences.create(plugin)

    @Provides
    fun MiniMessage(): MiniMessage = MiniMessage.miniMessage()

    @Provides
    fun Server(plugin: Plugin): Server = plugin.server

    @Provides
    fun Logger(plugin: Plugin): Logger = plugin.slF4JLogger

    @Singleton
    @Provides
    fun provideLuckPerms(plugin: Plugin): LuckPerms {
        return plugin.server.servicesManager.load(LuckPerms::class.java)
            ?: throw IllegalStateException("LuckPerms not found!")
    }

}
