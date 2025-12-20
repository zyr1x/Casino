package ru.lewis.casino.model.animation

import com.google.inject.Inject
import com.google.inject.Singleton
import com.onarandombox.MultiverseCore.MultiverseCore
import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import eu.decentsoftware.holograms.api.holograms.HologramLine
import eu.decentsoftware.holograms.api.utils.items.HologramItem
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import ru.lewis.casino.configuration.type.Slot
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.casino.extension.legacy
import java.util.*
import java.util.function.Consumer
import kotlin.math.cos
import kotlin.math.sin

@Singleton
class SpinAnimation @Inject constructor(
    private val configurationService: ConfigurationService,
    private val multiverseCore: MultiverseCore,
    private val plugin: Plugin,
) {
    private val config get() = configurationService.config
    private val center get() = configurationService.config.location.getLocation(multiverseCore) // <- всегда клон
    private val radius get() = configurationService.config.radius
    private val isAxisX get() = configurationService.config.isAxisX

    private lateinit var entities: MutableList<Entity>

    fun reload() {
        entities.forEach { it.hologram.delete() }
        entities.clear()
        spawn()
    }

    fun spawn() {
        val weightedSlots = distributeSlotsEvenly(config.slots)
        val angleStep = 2 * Math.PI / weightedSlots.size
        var angle = 0.0

        entities = weightedSlots.map { slot ->
            val entity = Entity(createHologram(slot, center), slot, angle)
            angle += angleStep
            entity
        }.toMutableList()

        updatePositions(center, radius, 0.0)
    }

    fun spin(callback: (Slot) -> Unit) {
        val winner = entities.random()
        val rounds = 3
        val targetAngle = (2 * Math.PI * rounds) + (Math.PI / 2 - winner.angle)

        var currentAngle = 0.0
        val step = 0.1

        object : BukkitRunnable() {
            override fun run() {
                if (currentAngle >= targetAngle) {
                    callback(winner.slot)
                    cancel()
                    return
                }

                currentAngle += step
                updatePositions(center, radius, currentAngle)
            }
        }.runTaskTimer(plugin, 1L, 1L)
    }

    private fun updatePositions(center: Location, radius: Int, offset: Double) {
        entities.forEach { entity ->
            val angle = entity.angle + offset
            val x: Double
            var y: Double
            var z: Double

            if (isAxisX) {
                x = center.x + radius * cos(angle)
                y = center.y + radius * sin(angle)
                z = center.z
            } else {
                x = center.x
                y = center.y + radius * sin(angle)
                z = center.z + radius * cos(angle)
            }

            val loc = center.clone().apply {
                this.x = x
                this.y = y
                this.z = z
            }

            DHAPI.moveHologram(entity.hologram, loc)
        }
    }

    fun distributeSlotsEvenly(slots: List<Slot>): List<Slot> {
        val total = slots.sumOf { it.weight }
        val result = MutableList<Slot?>(total) { null }

        var index = 0
        for (slot in slots) {
            repeat(slot.weight) {
                while (result[index % total] != null) {
                    index++
                }
                result[index % total] = slot
                index += total / slot.weight
            }
        }

        return result.filterNotNull()
    }

    private fun createHologram(slot: Slot, location: Location): Hologram {
        val itemTemplate = slot.template
        val item = itemTemplate.toItem()
        val hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location)
        val hologramPage = hologram.getPage(0)
        val hologramItem = HologramItem.fromItemStack(item)

        val hologramLineTwo = when (hologramItem.material) {
            Material.PLAYER_HEAD -> {
                HologramLine(hologramPage, location, "#ICON: PLAYER_HEAD (${itemTemplate.skullTexture})")
            }
            else -> {
                HologramLine(hologramPage, location, "#ICON:" + hologramItem.content)
            }
        }

        itemTemplate.displayName?.let {
            val hologramLineOne = HologramLine(hologramPage, location, it.legacy())
            hologramPage.addLine(hologramLineOne)
        }

        hologramPage.addLine(hologramLineTwo)

        return hologram
    }

    data class Entity(
        val hologram: Hologram,
        val slot: Slot,
        var angle: Double
    )
}