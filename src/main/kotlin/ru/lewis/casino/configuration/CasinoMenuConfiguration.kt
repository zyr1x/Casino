package ru.lewis.casino.configuration

import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.configuration.type.ItemTemplate
import ru.lewis.casino.configuration.type.MenuConfig
import ru.lewis.casino.extension.asMiniMessageComponent

@ConfigSerializable
data class CasinoMenuConfiguration(
    val template: MenuConfig = MenuConfig(
        title = "<gray>Казино".asMiniMessageComponent(),
        structure = listOf(
            "# # # # # # # # #",
            "# . a b . c d . #",
            "# # # # # # # # #",
        ),
        customItems = mapOf(
            '#' to ItemTemplate(Material.RED_STAINED_GLASS_PANE)
        )
    )
)