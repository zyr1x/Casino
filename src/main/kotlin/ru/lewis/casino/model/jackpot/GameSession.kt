package ru.lewis.casino.model.jackpot

import com.google.inject.ImplementedBy
import org.bukkit.entity.Player
import ru.lewis.casino.model.Participant
import ru.lewis.casino.model.jackpot.impl.GameSessionImpl
import java.time.Duration

@ImplementedBy(GameSessionImpl::class)
interface GameSession {
    fun addPlayer(player: Player, bet: Int)
    fun removePlayer(player: Player)
    fun getParticipant(player: Player): Participant?
    fun getParticipants(): Set<Participant>
    fun hasBet(player: Player): Boolean

    fun getDurationForStart(): Duration

    fun startGame()
    fun endGame()
    fun isRunning(): Boolean

    fun open(player: Player)
}