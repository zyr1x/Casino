package ru.lewis.casino

import com.google.inject.Inject
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.casino.model.SmartLifoCompositeTerminable
import ru.lewis.casino.model.TableRegistry
import ru.lewis.casino.service.CommandService
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.casino.service.VaultService
import xyz.xenondevs.invui.InvUI

class Main @Inject constructor(
    private val configurationService: ConfigurationService,
    private val commandService: CommandService,
    private val bukkitAudiences: BukkitAudiences,
    private val tableRegistry: TableRegistry,
    private val vaultService: VaultService,
    private val plugin: Plugin,
    logger: Logger,
) {

    private val terminableRegistry = SmartLifoCompositeTerminable(logger)

    fun start() {
        audiences = bukkitAudiences
        InvUI.getInstance().setPlugin(plugin)

        terminableRegistry.apply {
            with(bukkitAudiences)
            bindModule(configurationService)
            bindModule(tableRegistry)
            bindModule(commandService)
            bindModule(vaultService)
        }
    }

    fun stop() {
        terminableRegistry.closeAndReportException()
    }

    companion object {
        lateinit var audiences: BukkitAudiences
    }
}
