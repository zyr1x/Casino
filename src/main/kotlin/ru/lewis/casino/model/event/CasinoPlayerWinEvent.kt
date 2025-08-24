package ru.lewis.casino.model.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import ru.lewis.casino.configuration.type.Slot

class CasinoPlayerWinEvent(
    player: Player,
    val amount: Int,
    val slot: Slot
) : PlayerEvent(player) {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        @JvmStatic
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}