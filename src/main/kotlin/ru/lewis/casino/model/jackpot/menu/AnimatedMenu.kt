package ru.lewis.casino.model.jackpot.menu

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import org.bukkit.entity.Player
import ru.lewis.casino.configuration.type.import
import ru.lewis.casino.model.AssistedInjectFactories
import ru.lewis.casino.model.Participant
import ru.lewis.casino.model.jackpot.GameSession
import ru.lewis.casino.model.menu.animation.CustomHorizontalSnakeAnimation
import ru.lewis.casino.service.ConfigurationService
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

class AnimatedMenu @Inject constructor(
    private val assistedInjectFactories: AssistedInjectFactories,
    private val configurationService: ConfigurationService,
    @Assisted private val gameSession: GameSession,
    @Assisted private val winner: Participant
) : Menu {
    private val menu get() = configurationService.menus.animation
    private lateinit var gui: PagedGui<Item>

    override fun openMenu(player: Player) {
        if (!init(player)) {
            doOpen(player)
        }
    }

    override fun getGui(): PagedGui<Item> = gui

    private fun init(viewer: Player): Boolean {
        if (!::gui.isInitialized) {
            Window.single().apply {
                gui = import(menu) {
                    menu.templates['.']?.let {
                        setContent(
                            listOf(
                                assistedInjectFactories.createPlayerButton(it, winner)
                            )
                        )
                    }
                }
                open(viewer)
            }.setViewer(viewer).build()
            val animation = CustomHorizontalSnakeAnimation(1, true, gameSession)
            gui.playAnimation(animation) { true }
            return true
        }
        return false
    }

    private fun doOpen(player: Player) {
        Window.single().setGui(gui).open(player)
    }
}