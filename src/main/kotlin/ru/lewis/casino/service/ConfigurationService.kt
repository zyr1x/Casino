package ru.lewis.casino.service

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.Material
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.slf4j.Logger
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.extensions.set
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

import ru.lewis.casino.configuration.CasinoMenuConfiguration
import ru.lewis.casino.configuration.Configuration
import ru.lewis.casino.configuration.LocalizationConfiguration
import ru.lewis.casino.configuration.serializer.*
import ru.lewis.casino.configuration.type.MiniMessageComponent
import java.awt.Color
import java.time.Duration
import kotlin.io.path.*

@Singleton
class ConfigurationService @Inject constructor(
    private val plugin: Plugin,
    private val materialSerializer: MaterialSerializer,
    private val durationSerializer: DurationSerializer,
    private val miniMessageComponentSerializer: MiniMessageComponentSerializer,
    private val colorSerializer: ColorSerializer,
    private val potionEffectSerializer: PotionEffectSerializer,
    private val enchantmentSerializer: EnchantmentSerializer,
    private val attributeModifierSerializer: AttributeModifierSerializer,
    private val potionEffectTypeSerializer: PotionEffectTypeSerializer,
    private val logger: Logger,
) : Service {

    lateinit var config: Configuration
        private set

    lateinit var casinoMenu: CasinoMenuConfiguration
        private set

    lateinit var localization: LocalizationConfiguration
        private set

    private val path = plugin.dataFolder.toPath()
    private val settingsFile = path.resolve("settings.yml")
    private val casinoMenuFile = path.resolve("casino-menu.yml")
    private val localizationFile = path.resolve("localization.yml")

    override fun run() = doReload()

    fun saveConfig() {
        createLoaderBuilder().path(settingsFile).build().let {
            it.save(it.createNode().set(config))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    @Synchronized
    private fun doReload() {
        plugin.dataFolder.toPath().createDirectories()

        config = createLoaderBuilder().path(settingsFile).build().getAndSave<Configuration>()
        casinoMenu = createLoaderBuilder().path(casinoMenuFile).build().getAndSave<CasinoMenuConfiguration>()
        localization = createLoaderBuilder().path(localizationFile).build().getAndSave<LocalizationConfiguration>()
    }

    private fun createLoaderBuilder(): YamlConfigurationLoader.Builder {
        return YamlConfigurationLoader.builder()
            .defaultOptions {
                it.serializers { serializers ->
                    serializers
                        .register(MiniMessageComponent::class.java, miniMessageComponentSerializer)
                        .register(Duration::class.java, durationSerializer)
                        .register(Material::class.java, materialSerializer)
                        .register(Color::class.java, colorSerializer)
                        .register(PotionEffect::class.java, potionEffectSerializer)
                        .register(Enchantment::class.java, enchantmentSerializer)
                        .register(AttributeModifier::class.java, attributeModifierSerializer)
                        .register(PotionEffectType::class.java, potionEffectTypeSerializer)
                        .registerAnnotatedObjects(objectMapperFactory())
                }
            }
            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
    }

    private inline fun <reified T : Any> YamlConfigurationLoader.getAndSave(): T {
        val obj = this.load().get(T::class)!!
        this.save(this.createNode().set(T::class, obj))
        return obj
    }

}

