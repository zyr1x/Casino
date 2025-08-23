package ru.lewis.casino.service

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import org.bukkit.plugin.Plugin
import ru.lewis.casino.bootstrap.CasinoPlugin
import ru.lewis.casino.command.CasinoCommand
import ru.lewis.core.service.Service

@Singleton
class CommandService @Inject constructor(
    @CasinoPlugin private val plugin: Plugin,
    private val casinoCommand: CasinoCommand
) : Service {
    override fun run() {
        LiteBukkitFactory.builder(plugin.name, plugin)
            .commands(casinoCommand)
            .build()
    }
}