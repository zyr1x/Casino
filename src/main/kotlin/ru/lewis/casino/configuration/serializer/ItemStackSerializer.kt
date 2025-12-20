package ru.lewis.casino.configuration.serializer

import com.google.inject.Inject
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class ItemStackSerializer @Inject constructor() : TypeSerializer<ItemStack> {
    override fun deserialize(type: Type, node: ConfigurationNode): ItemStack? {
        return ItemStack.deserializeBytes(node.get<ByteArray>())
    }

    override fun serialize(type: Type, obj: ItemStack?, node: ConfigurationNode) {
        node.set(obj?.serializeAsBytes())
    }
}