package ru.lewis.casino.model.jackpot.animation

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import ru.lewis.casino.configuration.type.ItemTemplate
import ru.lewis.casino.model.AssistedInjectFactories
import ru.lewis.casino.model.Participant
import ru.lewis.casino.model.jackpot.GameSession
import ru.lewis.casino.service.ConfigurationService
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.Item

class SpinAnimation @Inject constructor(
    private val configurationService: ConfigurationService,
    private val assistedInjectFactories: AssistedInjectFactories,
    private val plugin: Plugin,
    @Assisted private val gameSession: GameSession,
    @Assisted private val winner: Participant
) : Animation {
    private val menu get() = configurationService.menus.main
    private lateinit var task: BukkitRunnable
    private lateinit var gui: PagedGui<Item>

    override fun play(gui: PagedGui<Item>) {
        this.gui = gui
        this.task = Task()

        menu.templates['.']?.let { item ->
            this.gui.setContent(
                listOf(
                    assistedInjectFactories.createPlayerButton(item, winner),
                    assistedInjectFactories.createPlayerButton(ItemTemplate(Material.DIRT), winner),
                    assistedInjectFactories.createPlayerButton(ItemTemplate(Material.DIAMOND), winner),
                    assistedInjectFactories.createPlayerButton(ItemTemplate(Material.ARROW), winner)
                )
            )
        }
    }

    inner class Task: BukkitRunnable() {
        init {
            this.runTaskTimer(plugin, 0L, 10L)
        }

        override fun run() {
            this.swap()
        }

        private fun swap() {
            val participants = gui.contentListSlots.toList()
            val iterator = participants.iterator()

            while (iterator.hasNext()) {
                val current = iterator.next()
                val next = if (iterator.hasNext()) iterator.next() else null
                val slotElement = gui.getSlotElement(current)

                next?.let {
                    gui.setSlotElement(it, slotElement)
                }
            }
        }

    }

}