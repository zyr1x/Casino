package ru.lewis.casino.configuration.type

import com.destroystokyo.paper.profile.ProfileProperty
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.extension.asMiniMessageComponent
import ru.lewis.casino.extension.toAwtColor
//import su.funtime.FunEnchantments
//import su.funtime.enchantscore.MEnchant
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper
import xyz.xenondevs.invui.InvUI
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.PotionBuilder
import java.awt.Color
import java.util.*

@ConfigSerializable
data class ItemTemplate(
    val type: Material,
    val amount: Int? = null,
    val displayName: MiniMessageComponent? = null,
    val lore: List<MiniMessageComponent>? = null,
    val flags: Set<ItemFlag>? = null,
    val enchantments: Map<Enchantment, Int>? = null,
    val unbreakable: Boolean? = null,
    val potion: PotionTemplate? = null,
    val skullTexture: String? = null,
    val storedEnchantments: Map<Enchantment, Int>? = null,
    val attributes: List<AttributeConfiguration>? = null,
    val persistentDataContainer: Map<String, String>? = null,
//    val customEnchantments: Map<MEnchant, Int>? = null
) {

    @ConfigSerializable
    data class PotionTemplate(
        val type: PotionType,
        val extended: Boolean,
        val upgraded: Boolean,
        val color: Color?,
        val effects: List<PotionEffect>?
    )

    fun resolve(vararg tagResolvers: TagResolver): ItemTemplate {
        return copy(
            displayName = displayName?.resolve(*tagResolvers),
            lore = lore?.map { line ->
                (line as MiniMessageComponent?)?.resolve(*tagResolvers) ?: Component.empty().asMiniMessageComponent()
            }
        )
    }

    fun resolve(newLore: List<MiniMessageComponent>, vararg tagResolvers: TagResolver): ItemTemplate {
        return copy(
            displayName = displayName?.resolve(*tagResolvers),
            lore = (this.lore?.toMutableList()?.apply {
                addAll(newLore)
            } ?: newLore).map { line ->
                line.resolve(*tagResolvers)
            }
        )
    }

    fun toItem(): ItemStack {
        val builder = createBuilder()

        builder.amount = amount ?: 1

        displayName?.also { builder.setDisplayName(AdventureComponentWrapper(it.asComponent())) }

        if (lore != null) {
            builder.setLore(lore.map { AdventureComponentWrapper(it.asComponent()) })
        }

        if (flags != null) {
            builder.setItemFlags(flags.toMutableList())
        }

        enchantments?.forEach { (enchantment, level) ->
            builder.addEnchantment(enchantment, level, true)
        }

        builder.setUnbreakable(unbreakable ?: false)

        if (potion != null && builder is PotionBuilder) {

            potion.color?.let { builder.setColor(it) }

            potion.effects?.forEach { builder.addEffect(it) }

            builder.setBasePotionData(
                PotionData(
                    potion.type,
                    potion.extended,
                    potion.upgraded
                )
            )
        }

        val item = builder.get()

        if (skullTexture != null) {
            setSkullTexture(item, skullTexture)
        }

        if (storedEnchantments != null) {
            (item.itemMeta as? EnchantmentStorageMeta)?.let {
                storedEnchantments.forEach { (enchantment, level) -> it.addStoredEnchant(enchantment, level, true) }
                item.setItemMeta(it)
            }
        }

        if (attributes != null) {
            item.itemMeta = item.itemMeta.apply {
                attributes.forEach {
                    addAttributeModifier(it.attribute, it.modifier)
                }
            }
        }

        persistentDataContainer?.let {
            applyPersistentDataContainer(it, item)
        }

//        customEnchantments?.let {
//            it.forEach { (mEnchantment, level) ->
//                item.itemMeta = FunEnchantments.getInstance().enchantManager.applyEnchant(item.itemMeta, mEnchantment, level)
//            }
//        }

        return item
    }

    private fun createBuilder(): AbstractItemBuilder<*> {
        return when (type) {
            Material.POTION -> PotionBuilder(PotionBuilder.PotionType.NORMAL)
            Material.SPLASH_POTION -> PotionBuilder(PotionBuilder.PotionType.SPLASH)
            Material.LINGERING_POTION -> PotionBuilder(PotionBuilder.PotionType.LINGERING)
            Material.TIPPED_ARROW -> PotionBuilder(ItemStack(Material.TIPPED_ARROW))
            else -> ItemBuilder(type)
        }
    }

    companion object {
        fun applyPersistentDataContainer(persistentDataContainer: Map<String, String>, itemStack: ItemStack) {
            persistentDataContainer.forEach { (key, value) -> setCustomString(itemStack, key, value) }
        }

        fun setCustomString(item: ItemStack, keyName: String, value: String) {
            val meta = item.itemMeta ?: return
            val key = NamespacedKey(InvUI.getInstance().plugin, keyName)
            meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)
            item.itemMeta = meta
        }

        fun getCustomString(item: ItemStack, keyName: String): String? {
            val meta = item.itemMeta ?: return null
            val key = NamespacedKey(InvUI.getInstance().plugin, keyName)
            return meta.persistentDataContainer.get(key, PersistentDataType.STRING)
        }

        private fun setSkullTexture(itemStack: ItemStack, skullTexture: String?) {
            if (skullTexture == null) return
            val itemMeta = itemStack.itemMeta

            if (itemMeta is SkullMeta) {
                val profile = Bukkit.createProfile(UUID.randomUUID())
                profile.setProperty(ProfileProperty("textures", skullTexture))
                itemMeta.playerProfile = profile
            }
            itemStack.setItemMeta(itemMeta)
        }

        @OptIn(ExperimentalStdlibApi::class)
        @Suppress("DEPRECATION")
        fun fromItem(item: ItemStack): ItemTemplate {
            val displayName: MiniMessageComponent?
            val lore: List<MiniMessageComponent>?
            val flags: Set<ItemFlag>?
            val enchantments: Map<Enchantment, Int>?
            val unbreakable: Boolean?
            val potionTemplate: PotionTemplate?
            var skullTexture: String? = null
            val storedEnchantments: Map<Enchantment, Int>?
            val attributes: MutableList<AttributeConfiguration>?
            var persistentDataContainer: Map<String, String>? = null
//            val customEnchantments = getCustomEnchantments(item)

            with(item.itemMeta) {
                displayName = if (this.hasDisplayName()) {
                    this.displayNameComponent.toAdventure().asMiniMessageComponent()
                } else {
                    null
                }

                lore = this.lore()?.map { it.asMiniMessageComponent() }

                flags = this.itemFlags.takeIf { it.isNotEmpty() }

                enchantments = this.enchants.takeIf { it.isNotEmpty() }

                unbreakable = this.isUnbreakable.takeIf { it }

                potionTemplate = if (this is PotionMeta) {
                    PotionTemplate(
                        this.basePotionData.type,
                        this.basePotionData.isExtended,
                        this.basePotionData.isUpgraded,
                        this.color?.toAwtColor(),
                        if (this.hasCustomEffects()) this.customEffects else null
                    )
                } else null

                storedEnchantments = if (this is EnchantmentStorageMeta) this.storedEnchants else null

                if (item.type == Material.PLAYER_HEAD) {
                    val meta = item.itemMeta as SkullMeta
                    val profile = meta.playerProfile
                    skullTexture = profile?.properties
                        ?.firstOrNull { property -> property.name == "textures" }
                        ?.value
                }


                if (!this.hasAttributeModifiers()) {
                    attributes = null
                } else {
                    attributes = mutableListOf()
                    Attribute.entries.forEach { attribute ->
                        this.getAttributeModifiers(attribute)?.forEach { modifier ->
                            attributes += AttributeConfiguration(attribute, modifier)
                        }
                    }
                }

                getAllStringsFromItem(item, InvUI.getInstance().plugin).let {
                    if (it.isNotEmpty()) persistentDataContainer = it
                }
            }

            return ItemTemplate(
                item.type,
                item.amount.takeIf { it != 1 },
                displayName,
                lore,
                flags,
                enchantments,
                unbreakable,
                potionTemplate,
                skullTexture,
                storedEnchantments,
                attributes,
                persistentDataContainer,
//                customEnchantments
            )
        }

//        fun getCustomEnchantments(item: ItemStack): Map<MEnchant, Int>? {
//            val result = mutableMapOf<MEnchant, Int>()
//
//            val meta = item.itemMeta ?: return result
//            val pdc = meta.persistentDataContainer
//            val enchantManager = FunEnchantments.getInstance().enchantManager
//            val registered = enchantManager.enchantsNameSpaced
//
//            // Перебираем все зарегистрированные энчанты
//            for ((key, enchant) in registered) {
//                if (pdc.has(key, PersistentDataType.INTEGER)) {
//                    val level = pdc.get(key, PersistentDataType.INTEGER) ?: continue
//                    if (level > 0) result[enchant] = level
//                }
//            }
//
//            return result.ifEmpty { null }
//        }

        private fun getAllStringsFromItem(item: ItemStack?, plugin: Plugin): Map<String, String> {
            if (item == null || !item.hasItemMeta()) return emptyMap()

            val meta = item.itemMeta!!
            val container = meta.persistentDataContainer

            return container.keys
                .filter { key -> key.namespace == plugin.name.lowercase() && container.has(key, PersistentDataType.STRING) }
                .associate { key ->
                    val value = container.get(key, PersistentDataType.STRING) ?: ""
                    key.key to value
                }
        }
    }
}

fun Array<out BaseComponent>.toAdventure(): Component {
    return BungeeComponentSerializer.get().deserialize(this).compact()
}

