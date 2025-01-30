package ru.lewis.casino.configuration.type

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class Area(
    val location: LocationTemplate = LocationTemplate(),
    val radius: Double = 5.0
)