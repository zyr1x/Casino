package ru.lewis.casino.model.jackpot.menu

import com.google.inject.assistedinject.Assisted
import jakarta.inject.Inject
import org.bukkit.entity.Player
import ru.lewis.casino.configuration.type.import
import ru.lewis.casino.configuration.type.playAnim
import ru.lewis.casino.model.AssistedInjectFactories
import ru.lewis.casino.model.jackpot.GameSession
import ru.lewis.casino.model.menu.button.InfoButton
import ru.lewis.casino.model.menu.button.NextPageButton
import ru.lewis.casino.model.menu.button.PreviousPageButton
import ru.lewis.casino.service.ConfigurationService
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

class DefaultMenu @Inject constructor(
    private val assistedInjectFactories: AssistedInjectFactories,
    private val configurationService: ConfigurationService,
    @Assisted private val gameSession: GameSession
) : Menu {
    private val menu get() = configurationService.menus.main
    private lateinit var gui: PagedGui<Item>

    override fun openMenu(player: Player) {
        Window.single().apply {
            gui = import(menu) {
                menu.templates['i']?.let {
                    addIngredient('i', InfoButton(it, gameSession))
                }
                menu.templates['>']?.let {
                    addIngredient('>', NextPageButton(it))
                }
                menu.templates['<']?.let {
                    addIngredient('<', PreviousPageButton(it))
                }
                menu.templates['.']?.let { item ->
                    setContent(
                        gameSession.getParticipants().map { assistedInjectFactories.createPlayerButton(item, it) })
                }
            }
            open(player)
            gui.playAnim()
        }
    }

    override fun getGui(): PagedGui<Item> = gui
}