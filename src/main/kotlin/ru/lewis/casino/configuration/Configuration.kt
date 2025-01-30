package ru.lewis.casino.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.configuration.type.Area
import ru.lewis.casino.configuration.type.Dealer
import java.time.Duration

@ConfigSerializable
data class Configuration(
    val dealers: List<Dealer> = listOf(Dealer())
)