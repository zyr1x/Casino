package ru.lewis.casino.command

import com.google.inject.Inject
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import org.bukkit.entity.Player
import ru.lewis.casino.model.SpinThread
import ru.lewis.casino.model.menu.Menu
import ru.lewis.casino.service.ConfigurationService

@Command(name = "casino", aliases = ["bet"])
class CasinoCommand @Inject constructor(
    private val menu: Menu,
    private val configurationService: ConfigurationService,
    private val spinThread: SpinThread
){
    @Execute
    fun execute(@Context player: Player) {
        menu.openMenu(player)
    }

    @Execute(name = "reload")
    @Permission("casino.reload")
    fun reload() {
        configurationService.run()
        spinThread.reload()
    }
}