package ru.lewis.casino.model

import ru.lewis.casino.configuration.type.Slot
import java.util.UUID

data class Bet(
    val playerId: UUID,
    val amount: Int,
    val slot: Slot
)