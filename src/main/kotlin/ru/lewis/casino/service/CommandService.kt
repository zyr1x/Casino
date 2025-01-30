package ru.lewis.casino.service

import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import jakarta.inject.Inject
import jakarta.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import org.bukkit.plugin.Plugin
import ru.lewis.casino.command.BetCommand
import ru.lewis.casino.command.CasinoCommand

@Singleton
class CommandService @Inject constructor(
    private val plugin: Plugin,
    private val casinoCommand: CasinoCommand,
    private val betCommand: BetCommand
) : TerminableModule {
    override fun setup(consumer: TerminableConsumer) {
        LiteBukkitFactory.builder(plugin.name, plugin)
            .commands(casinoCommand, betCommand)
            .build()
    }
}