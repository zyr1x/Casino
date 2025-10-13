package ru.lewis.casino.model.listener

import com.google.inject.Inject
import com.google.inject.Singleton
import io.papermc.paper.event.player.AsyncChatEvent
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import ru.lewis.casino.model.BetManager
import ru.lewis.casino.model.BetWaitingManager
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.core.extension.legacy
import ru.lewis.core.extension.number
import ru.lewis.core.service.game.GameService

@Singleton
class BetListener @Inject constructor(
    private val betWaitingManager: BetWaitingManager,
    private val betManager: BetManager,
    private val configurationService: ConfigurationService,
    private val gameService: GameService
): Listener {
    private val messages get() = configurationService.localization.messages
    private val vaultRepository get() = gameService.getVaultRepository()

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player

        if (!betWaitingManager.hasPlayer(player)) return
        val message = event.originalMessage().legacy()

        try {
            val amount = message.toInt()
            val slot = betWaitingManager.getSlot(player)!!
            val economyResponse = vaultRepository.withdrawPlayer(player, amount.toDouble())

            if (economyResponse.type == EconomyResponse.ResponseType.FAILURE) {
                player.sendMessage(messages.errors.notMoney)
                return
            }
            betWaitingManager.removePlayer(player)

            player.sendMessage(messages.notifications.successfullyBet.resolve(slot.toPlaceholder(), number("amount", amount)))
        } catch (ignored: NumberFormatException) {
            player.sendMessage(messages.errors.onlyNumbers)
        }

        betWaitingManager.removePlayer(player)
    }
}