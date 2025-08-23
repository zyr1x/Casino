package ru.lewis.casino.configuration.type

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.core.configuration.type.ItemTemplate
import ru.lewis.core.extension.number

@ConfigSerializable
data class Slot(
    val id: Char = 'a',
    val multiplier: Int = 2,
    val template: ItemTemplate = ItemTemplate(
        Material.PLAYER_HEAD,
        skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThjY2ExZTBmYjVkOWJmNzdmNTI3MzU2ZThiZjRlNTNjYjRhNGM1NmYxMWU3NzlhYmFkYWU1NDFiYmVkYzYifX19"
    ),
    val weight: Int = 12
) {
    fun toPlaceholder(): TagResolver = TagResolver.resolver(
        number("multiplier", multiplier)
    )
}