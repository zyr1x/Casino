package ru.lewis.casino.model.menu

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.entity.Player
import ru.lewis.casino.model.AssistedInjectFactories
import ru.lewis.casino.service.ConfigurationService
import ru.lewis.core.configuration.type.import
import ru.lewis.core.configuration.type.playAnim
import xyz.xenondevs.invui.window.Window

@Singleton
class Menu @Inject constructor(
    private val configurationService: ConfigurationService,
    private val assistedInjectFactories: AssistedInjectFactories
){
    private val menu get() = configurationService.casinoMenu.template
    private val slots get() = configurationService.config.slots

    fun openMenu(player: Player) {
        Window.single().apply {
            val gui = import(menu) {
                slots.forEach {
                    addIngredient(it.id, assistedInjectFactories.createBetButton(it))
                }
            }
            open(player)
            gui.playAnim()
        }
    }
}