package ru.lewis.casino.model.jackpot.animation

import com.google.inject.ImplementedBy
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.Item

@ImplementedBy(SpinAnimation::class)
interface Animation {
    fun play(gui: PagedGui<Item>)
}