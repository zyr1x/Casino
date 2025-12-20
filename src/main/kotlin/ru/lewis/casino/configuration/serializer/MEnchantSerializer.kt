package ru.lewis.casino.configuration.serializer//package ru.lewis.casino.configuration.serializer
//
//import org.spongepowered.configurate.ConfigurationNode
//import org.spongepowered.configurate.serialize.TypeSerializer
//import su.funtime.FunEnchantments
//import su.funtime.enchantscore.MEnchant
//import java.lang.reflect.Type
//
//class MEnchantSerializer : TypeSerializer<MEnchant> {
//
//    @Suppress("DEPRECATION")
//    override fun deserialize(type: Type, node: ConfigurationNode): MEnchant? {
//        return node.string?.let {
//            FunEnchantments.getInstance().enchantManager.enchantsNameSpaced.get(
//                FunEnchantments.getInstance().enchantManager.enchantsNames.get(
//                    it.lowercase()
//                )
//            )
//        }
//    }
//
//    @Suppress("DEPRECATION")
//    override fun serialize(type: Type, obj: MEnchant?, node: ConfigurationNode) {
//        node.set(obj?.key)
//    }
//}