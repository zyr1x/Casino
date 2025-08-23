package ru.lewis.casino.model

import com.google.inject.Singleton
import org.bukkit.entity.Player
import ru.lewis.casino.configuration.type.Slot
import java.util.UUID

@Singleton
class BetWaitingManager {
    private val waitingBet: MutableMap<UUID, Slot> = mutableMapOf()

    fun addPlayer(player: Player, slot: Slot) {
        waitingBet[player.uniqueId] = slot
    }

    fun removePlayer(player: Player) {
        waitingBet.remove(player.uniqueId)
    }

    fun hasPlayer(player: Player): Boolean =
        waitingBet.contains(player.uniqueId)

    fun getSlot(player: Player): Slot? =
        waitingBet[player.uniqueId]
}