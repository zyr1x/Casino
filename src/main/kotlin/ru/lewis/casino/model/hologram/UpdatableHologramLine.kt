package ru.lewis.casino.model.hologram

import eu.decentsoftware.holograms.api.holograms.HologramLine
import eu.decentsoftware.holograms.api.holograms.HologramPage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import ru.lewis.casino.bootstrap.Bootstrap
import ru.lewis.casino.configuration.type.MiniMessageComponent
import ru.lewis.casino.extension.legacy
import java.lang.AutoCloseable
import java.util.function.Supplier

class UpdatableHologramLine(
    parent: HologramPage,
    location: Location,
    private val updatableLine: Supplier<MiniMessageComponent>
) : HologramLine(parent, location, "loading..."), AutoCloseable {
    private lateinit var bukkitTask: BukkitTask

    init {
        startUpdatable()
    }

    override fun close() {
        this.bukkitTask.cancel()
    }

    private fun startUpdatable() {
        this.bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            this.setContent(this.updatableLine.get().legacy())
        }, 0L, 20L)
    }

    companion object {
        val plugin: Plugin = JavaPlugin.getPlugin(Bootstrap::class.java)
    }
}