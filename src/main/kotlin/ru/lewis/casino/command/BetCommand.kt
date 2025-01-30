package ru.lewis.casino.command

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import jakarta.inject.Inject
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.entity.Player
import ru.lewis.casino.extension.adventure
import ru.lewis.casino.model.TableRegistry
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.casino.service.VaultService

@Command(name = "bet")
class BetCommand @Inject constructor(
    private val configurationService: ConfigurationService,
    private val vaultService: VaultService,
    private val tableRegistry: TableRegistry
){
    private val messages get() = configurationService.messages

    @Execute
    fun execute(@Context player: Player, @Arg("ставка") bet: Int) {
        tableRegistry.tables.forEach { table ->
            if (!table.isNearby(player)) {
                return@forEach
            }
            val gameSession = table.getGameSession()
            gameSession?.let {
                if (it.isRunning()) {
                    player.adventure.sendMessage(messages.errors.rouletteIsStart)
                    return
                }
                if (it.hasBet(player)) {
                    player.adventure.sendMessage(messages.errors.repeatBet)
                    return
                }
                val dealer = table.getDealer()
                val minBet = dealer.minBet
                if (bet < minBet) {
                    player.adventure.sendMessage(messages.errors.minBetErrors.resolve(
                        Formatter.number(
                            "min-bet",
                            minBet
                        )
                    ))
                    return
                }
                val maxBet = dealer.maxBet
                if (bet > maxBet) {
                    player.adventure.sendMessage(messages.errors.maxBetErrors.resolve(
                        Formatter.number(
                            "max-bet",
                            maxBet
                        )
                    ))
                    return
                }
                val response = vaultService.getEconomy().withdrawPlayer(player, bet.toDouble())
                if (response.type != EconomyResponse.ResponseType.SUCCESS) {
                    player.adventure.sendMessage(messages.errors.noMoney)
                    return
                }
                table.getGameSession()?.addPlayer(player, bet)
                player.adventure.sendMessage(messages.common.bet.resolve(
                    Formatter.number(
                        "bet",
                        bet
                    )
                ))
            }
        }
    }
}