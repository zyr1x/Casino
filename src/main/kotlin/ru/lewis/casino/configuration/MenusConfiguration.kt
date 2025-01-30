package ru.lewis.casino.configuration

import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.configuration.type.ItemTemplate
import ru.lewis.casino.configuration.type.MenuConfig
import ru.lewis.casino.configuration.type.MiniMessageComponent
import ru.lewis.casino.extension.asMiniMessageComponent

@ConfigSerializable
data class MenusConfiguration(
    val main: MenuConfig = MenuConfig(
        "<gray>Казино".asMiniMessageComponent(),
        structure = listOf(
            ". . . . . . . . .",
            ". . . . . . . . .",
            "x x x < i > x x x"
        ),
        templates = mapOf(
            '.' to ItemTemplate(
                type = Material.PLAYER_HEAD,
                displayName = "<player>".asMiniMessageComponent(),
                lore = listOf("<player-bet> <player-percent>".asMiniMessageComponent())
            ),
            'i' to ItemTemplate(
                type = Material.CLOCK,
                displayName = "Ждать осталось: <time>".asMiniMessageComponent()
            ),
            '>' to ItemTemplate(Material.ARROW),
            '<' to ItemTemplate(Material.ARROW)
        )
    ),
    val animation: MenuConfig = MenuConfig(
        "<gray>Казино".asMiniMessageComponent(),
        structure = listOf(
            ". . . . . . . . .",
            ". . . . . . . . .",
            "x x x x x x x x x"
        ),
        templates = mapOf(
            '.' to ItemTemplate(
                type = Material.PLAYER_HEAD,
                displayName = "<player>".asMiniMessageComponent(),
                lore = listOf("<player-bet> <player-percent>".asMiniMessageComponent())
            )
        )
    ),
    val playerBetLore: List<MiniMessageComponent> = listOf(
        "Ставка игрока: <bet>".asMiniMessageComponent(),
        "Шанс выигрыша: <percent>".asMiniMessageComponent()
    )
)


