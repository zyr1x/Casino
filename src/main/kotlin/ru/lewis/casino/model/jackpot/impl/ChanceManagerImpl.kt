package ru.lewis.casino.model.jackpot.impl

import ru.lewis.casino.model.Participant
import ru.lewis.casino.model.jackpot.ChanceManager
import ru.lewis.casino.model.jackpot.GameSession
import kotlin.random.Random

class ChanceManagerImpl(
    private val gameSession: GameSession
) : ChanceManager {

    override fun chance(percent: Int): Boolean {
        val randomValue = Random.nextInt(1000) / 10
        return randomValue <= percent
    }

    override fun calculatePercent(bet: Int): Int {
        val totalBet = gameSession.getParticipants().sumOf { it.bet }

        if (totalBet == 0) return 0

        return ((bet.toDouble() / totalBet.toDouble()) * 100).toInt()
    }

    override fun updatePercentAll() {
        val totalBet = gameSession.getParticipants().sumOf { it.bet }

        if (totalBet == 0) {
            gameSession.getParticipants().forEach { participant ->
                participant.percent = 0
            }
        } else {
            gameSession.getParticipants().forEach { participant ->
                participant.percent = ((participant.bet.toDouble() / totalBet.toDouble()) * 100).toInt()
            }
        }
    }

    override fun getWinner(): Participant? {
        val totalChance = gameSession.getParticipants().sumOf { it.percent }

        if (totalChance <= 0) return null

        val randomChance = Random.nextInt(1, 101)

        var currentChance = 0
        for (participant in gameSession.getParticipants()) {
            currentChance += participant.percent
            if (randomChance <= currentChance) {
                return participant
            }
        }

        return null
    }

    override fun getAllBet(): Int {
        var bank = 0
        gameSession.getParticipants().forEach { bank += it.bet }
        return bank
    }


}