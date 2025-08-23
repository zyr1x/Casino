package ru.lewis.casino.model

import com.google.inject.Singleton
import org.bukkit.entity.Player
import ru.lewis.casino.configuration.type.Slot
import java.util.UUID

@Singleton
class BetManager {
    private val bets: MutableMap<UUID, Bet> = mutableMapOf()

    fun bet(player: Player, amount: Int, slot: Slot) {
        val bet = Bet(player.uniqueId, amount, slot)
        bets[player.uniqueId] = bet
    }

    fun getBet(player: Player): Bet? =
        bets[player.uniqueId]

    fun removeBet(player: Player) {
        bets.remove(player.uniqueId)
    }

    fun clearBets() {
        bets.clear()
    }

    fun hasBet(player: Player) = bets.containsKey(player.uniqueId)

    fun allBets(): Collection<Bet> =
        bets.values
}