package ru.lewis.casino.model.menu.button

import ru.lewis.casino.configuration.type.ItemTemplate
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class PreviousPageButton(
    private val template: ItemTemplate
) : PageItem(false) {
    override fun getItemProvider(p0: PagedGui<*>?): ItemProvider {
        return ItemBuilder(template.toItem())
    }
}