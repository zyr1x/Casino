package ru.lewis.casino.configuration.type

import com.onarandombox.MultiverseCore.MultiverseCore
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
    private fun init(multiverseCore: MultiverseCore): Location? {
        val world = multiverseCore.mvWorldManager.getMVWorld(world).cbWorld ?: return null
        return Location(world, x, y, z, yaw, pitch)
    }

    fun getLocation(multiverseCore: MultiverseCore): Location {
        if (location == null) {
            location = init(multiverseCore)
        }
        return location?.clone() ?: throw IllegalStateException("Мир $world не загружен!")
    }

    fun teleport(player: Player, multiverseCore: MultiverseCore) {
        // Если location не инициализировано, инициализируем его
        if (location == null) {
            location = init(multiverseCore)
        }

        this.location?.let {
            player.teleportAsync(it)
        }
    }

    fun export(newLocation: Location, multiverseCore: MultiverseCore) {
        this.x = newLocation.x
        this.y = newLocation.y
        this.z = newLocation.z
        this.pitch = newLocation.pitch
        this.yaw = newLocation.yaw
        this.world = newLocation.world.name
        // Инициализируем location при изменении координат
        this.location = init(multiverseCore)
    }
}
