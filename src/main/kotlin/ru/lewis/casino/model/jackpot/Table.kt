package ru.lewis.casino.model.jackpot

import com.google.inject.ImplementedBy
import org.bukkit.entity.Player
import ru.lewis.casino.configuration.type.Dealer
import ru.lewis.casino.model.jackpot.impl.TableImpl

@ImplementedBy(TableImpl::class)
interface Table {
    fun getGameSession(): GameSession?
    fun updateGameSession()
    fun getDealer(): Dealer
    fun isNearby(player: Player): Boolean
}