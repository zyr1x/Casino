package ru.lewis.casino.bootstrap

import com.google.inject.Guice
import org.bukkit.plugin.java.JavaPlugin
import ru.lewis.casino.Main

class Bootstrap : JavaPlugin() {
    private var disabled: Boolean = false

    private lateinit var entryPoint: Main

    override fun onEnable() {
        try {
            entryPoint = Guice.createInjector(InjectionModule(this)).getInstance(Main::class.java)
            entryPoint.start()
        } catch (e: Throwable) {
            slF4JLogger.error("Failed to enable", e)
            server.scheduler.runTask(this, Runnable { server.pluginManager.disablePlugin(this) })
        }
    }

    override fun onDisable() {
        if (::entryPoint.isInitialized && !disabled) {
            disabled = true
            entryPoint.stop()
        }
    }
}
