package ru.lewis.casino.model

import org.bukkit.entity.Player

class Participant(
    val player: Player,
    var bet: Int,
    var percent: Int
) : Comparable<Participant> {
    override fun compareTo(other: Participant): Int {
        return percent.compareTo(other.percent)
    }
}
