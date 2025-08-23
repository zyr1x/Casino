package ru.lewis.casino.bootstrap

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import org.bukkit.plugin.Plugin
import ru.lewis.casino.model.AssistedInjectFactories

class InjectionModule(
    private val plugin: Plugin,
) : AbstractModule() {

    override fun configure() {
        install(
            FactoryModuleBuilder()
                .build(AssistedInjectFactories::class.java)
        )
    }

    @Provides
    @CasinoPlugin
    fun provideCasesPlugin(): Plugin = plugin
}
