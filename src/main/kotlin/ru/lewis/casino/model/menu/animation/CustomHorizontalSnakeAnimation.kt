package ru.lewis.casino.model.menu.animation

import ru.lewis.casino.model.jackpot.GameSession

class CustomHorizontalSnakeAnimation(
    tickDelay: Int,
    sound: Boolean = true,
    private val gameSession: GameSession
) : AbstractSoundAnimation(tickDelay, sound) {
    private var x = 0
    private var y = 0
    private var left = false

    override fun handleFrame(frame: Int) {
        var slotShown = false

        while (!slotShown) {
            val slotIndex = this.convToIndex(this.x, this.y)
            if (getSlots()!!.contains(slotIndex).also { slotShown = it }) {
                this.show(*intArrayOf(slotIndex))
            }

            if (this.left) {
                if (this.x <= 0) {
                    ++this.y
                    this.left = false
                } else {
                    --this.x
                }
            } else if (this.x >= this.getWidth() - 1) {
                ++this.y
                this.left = true
            } else {
                ++this.x
            }

            if (this.y >= this.getHeight()) {
                gameSession.endGame()
                this.finish()
                return
            }
        }
    }
}