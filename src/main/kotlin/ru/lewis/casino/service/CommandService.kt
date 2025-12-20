package ru.lewis.casino.service

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import org.bukkit.plugin.Plugin
import ru.lewis.casino.command.CasinoCommand

@Singleton
class CommandService @Inject constructor(
    private val plugin: Plugin,
    private val casinoCommand: CasinoCommand
) : Service {
    override fun run() {
        LiteBukkitFactory.builder(plugin.name, plugin)
            .commands(casinoCommand)
            .build()
    }
}