package ru.lewis.casino.command

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import jakarta.inject.Inject
import org.bukkit.entity.Player
import ru.lewis.casino.model.TableRegistry
import ru.lewis.casino.service.ConfigurationService

@Command(name = "casino", aliases = ["jackpot"])
class CasinoCommand @Inject constructor(
    private val configurationService: ConfigurationService,
    private val tableRegistry: TableRegistry
){

    @Execute
    fun execute(@Context player: Player) {
        tableRegistry.tables.forEach { it.getGameSession()?.open(player) }
    }

    @Execute(name = "reload")
    @Permission("casino.reload")
    fun reload() {
        configurationService.reload()
        tableRegistry.reload()
    }
}