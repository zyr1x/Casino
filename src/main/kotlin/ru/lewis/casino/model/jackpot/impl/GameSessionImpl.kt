package ru.lewis.casino.model.jackpot.impl

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import ru.lewis.casino.extension.broadcast
import ru.lewis.casino.model.AssistedInjectFactories
import ru.lewis.casino.model.Participant
import ru.lewis.casino.model.jackpot.ChanceManager
import ru.lewis.casino.model.jackpot.GameSession
import ru.lewis.casino.model.jackpot.Table
import ru.lewis.casino.model.jackpot.menu.AnimatedMenu
import ru.lewis.casino.model.jackpot.menu.DefaultMenu
import ru.lewis.casino.model.jackpot.menu.Menu
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.casino.service.VaultService
import xyz.xenondevs.invui.window.WindowManager
import java.time.Duration
import java.util.*

class GameSessionImpl @Inject constructor(
    private val assistedInjectFactories: AssistedInjectFactories,
    private val configurationService: ConfigurationService,
    private val vaultService: VaultService,
    private val plugin: Plugin,
    @Assisted private val table: Table
) : GameSession {
    private val messages get() = configurationService.messages
    private val players: MutableSet<Participant> = TreeSet()
    private var chanceManager: ChanceManager? = null
    private lateinit var animatedMenu: AnimatedMenu
    private lateinit var defaultMenu: DefaultMenu
    private lateinit var winner: Participant
    private var running: Boolean = false
    private var birth: Long? = null

    init {
        chanceManager = ChanceManagerImpl(this)
        defaultMenu = assistedInjectFactories.createDefaultMenu(this)
    }

    override fun addPlayer(player: Player, bet: Int) {
        chanceManager?.let {
            val percent = it.calculatePercent(bet)
            if (!players.add(Participant(player, bet, percent))) return
            it.updatePercentAll()

            if (players.size == table.getDealer().playersForStart) {
                startCountdown()
            }
        }
    }

    override fun removePlayer(player: Player) {
        players.removeIf { player.uniqueId == it.player.uniqueId }
    }

    override fun hasBet(player: Player): Boolean {
        val has = players.firstOrNull { predicate -> predicate.player.uniqueId == player.uniqueId }
        return has != null
    }

    override fun getParticipant(player: Player): Participant? =
        players.firstOrNull { player.uniqueId == it.player.uniqueId }

    override fun getParticipants(): Set<Participant> = players

    override fun startGame() {
        running = true

        chanceManager?.let { manager ->
            manager.getWinner().thenApplySync {
                it?.let {
                    this.winner = it
                    animatedMenu = assistedInjectFactories.createAnimatedMenu(this, winner)
                    this.openMenuForParticipants()
                }
            }
        }

        broadcast(messages.common.gameStartAnimation.resolve(
            Placeholder.component(
                "table",
                table.getDealer().name
            )
        ))
    }

    override fun endGame() {
        broadcast(
            messages.common.gameEnd.resolve(
                Placeholder.component(
                    "table",
                    this.table.getDealer().name
                ),
                Placeholder.unparsed(
                    "player",
                    winner.player.name
                ),
                Formatter.number(
                    "percent",
                    winner.percent
                )
            )
        )
        salary(winner.player)

        running = false
        this.table.updateGameSession()
        WindowManager.getInstance().windows.forEach { window -> window.close() }
    }

    override fun getDurationForStart(): Duration {
        if (birth == null) {
            return Duration.ZERO
        }

        val currentTimeMillis = System.currentTimeMillis()
        val passedTime = currentTimeMillis - birth!!
        val result = table.getDealer().waitPlayers.minusMillis(passedTime)

        return if (result.isNegative) Duration.ZERO else result
    }

    override fun isRunning(): Boolean = running

    override fun open(player: Player) {
        val menu: Menu = if (!isRunning()) defaultMenu else animatedMenu
        if (table.isNearby(player)) menu.openMenu(player)
    }

    private fun startCountdown() {
        birth = System.currentTimeMillis()
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            this.startGame()
        }, table.getDealer().waitPlayers.toSeconds() * 20L)

        broadcast(
            messages.common.gameStart.resolve(
                Placeholder.component(
                    "table",
                    this.table.getDealer().name
                )
            )
        )
    }

    private fun salary(player: Player) {
        chanceManager?.let {
            vaultService.getEconomy().depositPlayer(player, it.getAllBet().toDouble())
        }
    }

    private fun openMenuForParticipants() {
        chanceManager?.let {
            players.forEach { participant ->
                val player = participant.player
                open(player)
            }
        }
    }
}