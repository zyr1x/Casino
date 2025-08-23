package ru.lewis.casino.configuration

import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.configuration.type.Slot
import ru.lewis.core.configuration.type.ItemTemplate
import ru.lewis.core.configuration.type.LocationTemplate
import java.time.Duration

@ConfigSerializable
data class Configuration(
    val slots: List<Slot> = listOf(
        Slot(),
        Slot(
            id = 'b',
            multiplier = 3,
            template = ItemTemplate(
                Material.PLAYER_HEAD,
                skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmU5MDc2NzJhNDcwOTM4NTI1MDU4YzBlYTMxOWQ4YmU1YmEyOGQ5NzJjNTJmMmJkNTVkYmY0MTgyYjgifX19"
            ),
            weight = 6
        ),
        Slot(
            id = 'c',
            multiplier = 10,
            template = ItemTemplate(
                Material.PLAYER_HEAD,
                skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg4ZTEyNzA3Mjg0NDJhNDE2ZGY4ODVlODExODEyYjg3NGZlNzZiZDI5MjZjMWM0OTcyNzNkNTJmMjI3YmY3In19fQ=="
            ),
            weight = 10
        ),
        Slot(
            id = 'd',
            multiplier = 20,
            template = ItemTemplate(
                Material.PLAYER_HEAD,
                skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNjN2U3MWYxZGI5OTU5ZWVkZWJiZWY3OTkxZTdjNjgwYzgxZTY0ZGUyOGI1OTZjMTY0NGVlZGQwNDcifX19"
            ),
            weight = 1
        ),
    ),
    val spinDelay: Duration = Duration.ofMinutes(5),
    val location: LocationTemplate = LocationTemplate(-119.5, 105.0, -50.0, world = "spawn"),
    val hologramLocation: LocationTemplate = LocationTemplate(-119.5, 105.0, -50.0, world = "spawn"),
    val radius: Int = 2
)