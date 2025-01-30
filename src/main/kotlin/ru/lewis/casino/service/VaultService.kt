package ru.lewis.casino.service

import com.google.inject.Inject
import com.google.inject.Singleton
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.module.TerminableModule
import net.milkbowl.vault.economy.Economy
import org.bukkit.Server
import org.bukkit.plugin.RegisteredServiceProvider

@Singleton
class VaultService @Inject constructor(
    private val server: Server
): TerminableModule {

    private lateinit var econ: Economy

    override fun setup(consumer: TerminableConsumer) {
        setupEconomy()
    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp: RegisteredServiceProvider<Economy> = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        econ = rsp.provider
        return econ != null
    }

    fun getEconomy(): Economy = econ
}