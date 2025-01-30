package ru.lewis.casino.configuration.type

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class LocationTemplate(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var pitch: Float = 0.0f,
    var yaw: Float = 0.0f,
    var world: String = "world"
) {
    // Используем обычную переменную для location
    private var location: Location? = null

    // Инициализируем location при первом доступе
    private fun init(): Location? {
        val world = Bukkit.getWorld(world)
        return world?.let {
            Location(it, x, y, z, yaw, pitch)
        }
    }

    fun teleport(player: Player) {
        // Если location не инициализировано, инициализируем его
        if (location == null) {
            location = init()
        }

        this.location?.let {
            player.teleportAsync(it)
        }
    }

    fun export(newLocation: Location) {
        this.x = newLocation.x
        this.y = newLocation.y
        this.z = newLocation.z
        this.pitch = newLocation.pitch
        this.yaw = newLocation.yaw
        this.world = newLocation.world.name
        // Инициализируем location при изменении координат
        this.location = init()
    }

    fun bukkitLocation(): Location? {
        if (location == null) {
            location = init()
        }
        return location
    }
}
