package ru.lewis.casino.model.menu.button

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import ru.lewis.casino.configuration.type.ItemTemplate
import ru.lewis.casino.model.Participant
import ru.lewis.casino.service.ConfigurationService
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class PlayerButton @Inject constructor(
    private val configurationService: ConfigurationService,
    @Assisted private val template: ItemTemplate,
    @Assisted private val participant: Participant,
) : AbstractItem() {
    private val lore get() = configurationService.menus.playerBetLore

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(
            template.resolve(
                lore,
                Formatter.number(
                    "bet",
                    participant.bet
                ),
                Formatter.number(
                    "percent",
                    participant.percent
                ),
                Placeholder.unparsed(
                    "player",
                    participant.player.name
                )
            ).toItem()
        )
    }

    override fun handleClick(p0: ClickType, p1: Player, p2: InventoryClickEvent) {}
}