package ru.lewis.casino.model.jackpot.menu

import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.Item

interface Menu {
    fun openMenu(player: Player)
    fun getGui(): PagedGui<Item>
}