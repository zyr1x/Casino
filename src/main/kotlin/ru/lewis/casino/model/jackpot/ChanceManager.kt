package ru.lewis.casino.model.jackpot

import me.lucko.helper.promise.Promise
import ru.lewis.casino.model.Participant

interface ChanceManager {
    fun chance(percent: Int): Boolean
    fun calculatePercent(bet: Int): Int
    fun getWinner(): Promise<Participant?>
    fun updatePercentAll()
    fun getAllBet(): Int
}