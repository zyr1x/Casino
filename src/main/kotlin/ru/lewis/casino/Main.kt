package ru.lewis.casino

import com.google.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import ru.lewis.casino.hook.PlaceholderHook

import ru.lewis.casino.model.SpinThread
import ru.lewis.casino.model.listener.BetListener
import ru.lewis.casino.service.CommandService
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.casino.service.VaultService

class Main @Inject constructor(
    private val configurationService: ConfigurationService,
    private val commandService: CommandService,
    private val betListener: BetListener,
    private val plugin: Plugin,
    private val spinThread: SpinThread,
    private val vaultService: VaultService,
    private val placeholderHook: PlaceholderHook,
    logger: Logger,
) {
    fun start() {
        configurationService.run()
        commandService.run()

        registerListener()
        spinThread.start()
        vaultService.setupEconomy()
        placeholderHook.register()
    }

    fun stop() {
        placeholderHook.unregister()
    }

    fun registerListener() {
        Bukkit.getPluginManager().registerEvents(betListener, plugin)
    }
}
