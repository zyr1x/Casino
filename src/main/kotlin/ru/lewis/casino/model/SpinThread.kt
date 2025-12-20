package ru.lewis.casino.model

import com.google.inject.Inject
import com.google.inject.Singleton
import com.onarandombox.MultiverseCore.MultiverseCore
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import ru.lewis.casino.model.animation.SpinAnimation
import ru.lewis.casino.model.event.CasinoPlayerLoseEvent
import ru.lewis.casino.model.event.CasinoPlayerWinEvent
import ru.lewis.casino.model.hologram.HologramBuilder
import ru.lewis.casino.model.hologram.MyHologram
import ru.lewis.casino.model.hologram.UpdatableHologramLine
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.casino.extension.number
import ru.lewis.casino.service.VaultService
import kotlin.properties.Delegates

@Singleton
class SpinThread @Inject constructor(
    private val configurationService: ConfigurationService,
    private val spinAnimation: SpinAnimation,
    private val plugin: Plugin,
    private val betManager: BetManager,
    private val vaultService: VaultService,
    private val multiverseCore: MultiverseCore
) : BukkitRunnable() {
    private val config get() = configurationService.config
    private val messages get() = configurationService.localization.messages
    private val hologramConfiguration get() = configurationService.localization.hologram
    private val vaultRepository get() = vaultService.econ
    private val location get() = config.hologramLocation.getLocation(multiverseCore)
    private val isRemainingHologramEnable get() = config.isRemainingHologramEnable

    private var hologram: MyHologram? = null

    var remaining by Delegates.notNull<Long>()

    fun start() {
        spinAnimation.spawn()
        remaining = config.spinDelay.toSeconds()
        this.runTaskTimer(plugin, 20L, 20L)
    }

    fun reload() {
        spinAnimation.reload()
        remaining = config.spinDelay.toSeconds()

        hologram?.delete()

        if (isRemainingHologramEnable) {
            hologram = createSpinHologram()
        }
    }

    override fun run() {
        if (remaining <= 0) {
            if (isRemainingHologramEnable) {
                hologram?.delete()
                hologram = null
            }

            spinAnimation.spin {
                processBets(it)
                betManager.clearBets()
                remaining = config.spinDelay.toSeconds()
            }
        }
        if (isRemainingHologramEnable && hologram == null) hologram = createSpinHologram()

        remaining--
    }

    private fun createSpinHologram(): MyHologram {
        return HologramBuilder(location)
            .setLines { page ->
                hologramConfiguration.map { lineConf ->
                    UpdatableHologramLine(page, location) {
                        lineConf.resolve(
                            Placeholder.unparsed("time", formatRemaining(remaining))
                        )
                    }
                }
            }
            .build()
    }

    fun formatRemaining(remaining: Long): String =
        "%02d:%02d".format(remaining / 60, remaining % 60)

    private fun processBets(winSlot: ru.lewis.casino.configuration.type.Slot) {
        betManager.allBets().forEach { (playerId, amount, slot) ->
            val player = Bukkit.getPlayer(playerId)

            if (player != null && player.isOnline) {
                if (slot == winSlot) {
                    val winAmount = amount * slot.multiplier

                    vaultRepository.depositPlayer(player, winAmount.toDouble())
                    player.sendMessage(messages.notifications.betWin.resolve(number("amount", winAmount)))

                    Bukkit.getPluginManager().callEvent(CasinoPlayerWinEvent(player, amount, slot))
                } else {
                    player.sendMessage(messages.notifications.betLoss)

                    Bukkit.getPluginManager().callEvent(CasinoPlayerLoseEvent(player, amount, slot))
                }
            }
        }
    }
}
