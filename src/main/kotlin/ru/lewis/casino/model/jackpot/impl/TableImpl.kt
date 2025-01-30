package ru.lewis.casino.model.jackpot.impl

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import org.bukkit.entity.Player
import ru.lewis.casino.configuration.type.*
import ru.lewis.casino.model.AssistedInjectFactories
import ru.lewis.casino.model.jackpot.GameSession
import ru.lewis.casino.model.jackpot.Table

class TableImpl @Inject constructor(
    private val assistedInjectFactories: AssistedInjectFactories,
    @Assisted private val dealer: Dealer
) : Table {
    private var gameSession: GameSession? = null

    init {
        this.updateGameSession()
    }

    override fun updateGameSession() {
        gameSession = assistedInjectFactories.createGameSession(this)
    }

    override fun getGameSession(): GameSession? = gameSession

    override fun getDealer(): Dealer = dealer

    override fun isNearby(player: Player): Boolean {
        val dealerLocation = dealer.area.location.bukkitLocation()
        val playerLocation = player.location
        val distance = dealerLocation?.distance(playerLocation)
        val radius = dealer.area.radius
        distance?.let {
            if (it <= radius) {
                return true
            }
        }
        return false
    }
}