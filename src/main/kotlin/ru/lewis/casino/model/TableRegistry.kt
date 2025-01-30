package ru.lewis.casino.model

import jakarta.inject.Inject
import jakarta.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import ru.lewis.casino.model.jackpot.Table
import ru.lewis.casino.service.ConfigurationService

@Singleton
class TableRegistry @Inject constructor(
    private val assistedInjectFactories: AssistedInjectFactories,
    private val configurationService: ConfigurationService
): TerminableModule {
    val tables: MutableSet<Table> = mutableSetOf()
        get() = field

    override fun setup(consumer: TerminableConsumer) {
        reload()
    }

    fun reload() {
        tables.clear()
        tables.addAll(
            configurationService.config.dealers.map { dealer ->
                assistedInjectFactories.createTable(dealer)
            }
        )
    }
}