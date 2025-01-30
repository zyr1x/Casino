package ru.lewis.casino.model.jackpot

import ru.lewis.casino.model.Participant

interface ChanceManager {
    fun chance(percent: Int): Boolean
    fun calculatePercent(bet: Int): Int
    fun getWinner(): Participant?
    fun updatePercentAll()
    fun getAllBet(): Int
}