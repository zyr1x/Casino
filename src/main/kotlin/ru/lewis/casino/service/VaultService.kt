package ru.lewis.casino.service

import com.google.inject.Inject
import com.google.inject.Singleton
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredServiceProvider

@Singleton
class VaultService @Inject constructor(
    private val plugin: Plugin
){
    lateinit var econ: Economy

    fun setupEconomy(): Boolean {
        if (plugin.server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp: RegisteredServiceProvider<Economy?> =
            plugin.server.servicesManager.getRegistration(Economy::class.java) ?: return false
        econ = rsp.getProvider()
        return true
    }
}