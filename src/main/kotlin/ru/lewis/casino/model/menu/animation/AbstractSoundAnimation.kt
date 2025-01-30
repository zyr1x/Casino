package ru.lewis.casino.model.menu.animation

import org.bukkit.Sound

abstract class AbstractSoundAnimation(
    tickDelay: Int,
    sound: Boolean = true
): AbstractAnimation(tickDelay) {

    init {
        if (sound) {
            this.addShowHandler { frame: Int?, index: Int? ->
                for (viewer in this.getCurrentViewers()!!) {
                    viewer?.playSound(viewer.location, Sound.BLOCK_GLASS_STEP, 0.6f, 1.0f)
                }
            }
        }
    }

}