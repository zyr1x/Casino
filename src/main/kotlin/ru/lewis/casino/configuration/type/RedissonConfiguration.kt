package ru.lewis.casino.configuration.type

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class RedissonConfiguration(
    val address: String = "127.0.0.1",
    val port: Int = 6379,
    val password: String = ""
)