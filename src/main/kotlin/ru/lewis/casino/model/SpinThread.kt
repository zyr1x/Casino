package ru.lewis.casino.model

import com.google.inject.Inject
import com.google.inject.Singleton
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import ru.lewis.casino.bootstrap.CasinoPlugin
import ru.lewis.casino.model.animation.SpinAnimation
import ru.lewis.casino.model.event.CasinoPlayerLoseEvent
import ru.lewis.casino.model.event.CasinoPlayerWinEvent
import ru.lewis.casino.model.hologram.HologramBuilder
import ru.lewis.casino.model.hologram.MyHologram
import ru.lewis.casino.model.hologram.UpdatableHologramLine
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.core.extension.number
import ru.lewis.core.service.game.GameService
import kotlin.properties.Delegates

@Singleton
class SpinThread @Inject constructor(
    private val configurationService: ConfigurationService,
    private val spinAnimation: SpinAnimation,
    @CasinoPlugin private val plugin: Plugin,
    private val betManager: BetManager,
    private val gameService: GameService
) : BukkitRunnable() {
    private val config get() = configurationService.config
    private val messages get() = configurationService.localization.messages
    private val hologramConfiguration get() = configurationService.localization.hologram
    private val vaultRepository get() = gameService.getVaultRepository()
    private val location get() = config.hologramLocation.getLocation()

    private var remaining by Delegates.notNull<Long>()
    private var hologram: MyHologram? = null

    fun start() {
        spinAnimation.spawn()
        remaining = config.spinDelay.toSeconds()
        this.runTaskTimer(plugin, 20L, 20L)
    }

    fun reload() {
        spinAnimation.reload()
        remaining = config.spinDelay.toSeconds()
        hologram?.delete()
        hologram = createSpinHologram()
    }

    override fun run() {
        if (remaining <= 0) {
            hologram?.delete()
            hologram = null

            val winSlot = spinAnimation.spin()
            processBets(winSlot)
            betManager.clearBets()
            remaining = config.spinDelay.toSeconds()
            return
        }
        if (hologram == null) hologram = createSpinHologram()

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

    fun formatRemaining(remaining: Long): String {
        val minutes = remaining / 60
        val seconds = remaining % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

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
