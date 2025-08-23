package ru.lewis.casino.bootstrap

import com.google.inject.BindingAnnotation
import org.bukkit.plugin.java.JavaPlugin
import ru.lewis.casino.Main
import ru.lewis.core.bootstrap.Bootstrap

class Bootstrap : JavaPlugin() {
    private var disabled: Boolean = false

    private lateinit var entryPoint: Main

    override fun onEnable() {
        try {
            entryPoint = Bootstrap.injector.createChildInjector(InjectionModule(this)).getInstance(Main::class.java)
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

@BindingAnnotation
@Retention(AnnotationRetention.RUNTIME)
annotation class CasinoPlugin
