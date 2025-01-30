package ru.lewis.casino.configuration.type

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.extension.asMiniMessageComponent
import java.time.Duration

@ConfigSerializable
data class Dealer(
    val name: MiniMessageComponent = "Стол До 10к".asMiniMessageComponent(),
    val maxBet: Int = 10000,
    val minBet: Int = 500,
    val waitPlayers: Duration = Duration.ofSeconds(60),
    val area: Area = Area(),
    val playersForStart: Int = 1
)
