package ru.lewis.casino.model.menu.button

import io.github.blackbaroness.durationserializer.DurationFormats
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import ru.lewis.casino.configuration.type.ItemTemplate
import ru.lewis.casino.extension.format
import ru.lewis.casino.model.jackpot.GameSession
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AutoUpdateItem

class InfoButton(template: ItemTemplate, gameSession: GameSession) : AutoUpdateItem(
    20,
    {
        ItemBuilder(
            template.resolve(
                Placeholder.unparsed(
                    "time",
                    gameSession.getDurationForStart().format(DurationFormats.shortRussian())
                )
            ).toItem()
        )
    }
)