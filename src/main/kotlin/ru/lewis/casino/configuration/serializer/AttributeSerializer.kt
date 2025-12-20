package ru.lewis.casino.configuration.serializer

import com.google.inject.Inject
import org.bukkit.attribute.Attribute
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.set
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class AttributeSerializer @Inject constructor() : TypeSerializer<Attribute> {

    override fun deserialize(type: Type, node: ConfigurationNode): Attribute? {
        return node.string?.let { Attribute.valueOf(it) }
    }

    override fun serialize(type: Type, obj: Attribute?, node: ConfigurationNode) {
        node.set(String::class, obj?.name)
    }

}