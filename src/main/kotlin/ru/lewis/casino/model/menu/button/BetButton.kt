package ru.lewis.casino.model.menu.button

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import ru.lewis.casino.configuration.type.Slot
import ru.lewis.casino.model.BetManager
import ru.lewis.casino.model.BetWaitingManager
import ru.lewis.casino.service.ConfigurationService
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.ItemWrapper
import xyz.xenondevs.invui.item.impl.AbstractItem

class BetButton @Inject constructor(
    @Assisted private val slot: Slot,
    private val betManager: BetManager,
    private val betWaitingManager: BetWaitingManager,
    private val configurationService: ConfigurationService
) : AbstractItem() {
    private val messages get() = configurationService.localization.messages

    override fun getItemProvider(): ItemProvider? {
        return ItemWrapper(slot.template.toItem())
    }

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        if (betManager.hasBet(player)) {
            player.sendMessage(messages.errors.doubleBet)
            player.closeInventory()
            return
        }
        betWaitingManager.addPlayer(player, slot)
        player.sendMessage(messages.notifications.doBet.resolve(slot.toPlaceholder()))
        player.closeInventory()
    }
}