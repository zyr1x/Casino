package ru.lewis.casino.model.menu.animation

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import xyz.xenondevs.invui.InvUI
import xyz.xenondevs.invui.animation.Animation
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.util.SlotUtils
import xyz.xenondevs.invui.window.Window
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.stream.Collectors

abstract class AbstractAnimation(private val tickDelay: Int) : Animation {

    private val finishHandlers: MutableList<Runnable?> = ArrayList<Runnable?>()
    private var gui: Gui? = null
    private var width = 0
    private var height = 0
    private var windows: List<Window>? = null
    private var slots: CopyOnWriteArrayList<Int?>? = null
    private var show: BiConsumer<Int, Int>? = null
    private var task: BukkitTask? = null
    private var frame = 0
    private var noViewerTicks = 0

    override fun setGui(gui: Gui) {
        this.gui = gui
        this.width = gui.width
        this.height = gui.height
    }

    override fun setWindows(windows: List<Window>) {
        this.windows = windows
    }

    override fun addShowHandler(show: BiConsumer<Int, Int>) {
        if (this.show != null) {
            this.show = this.show!!.andThen(show)
        } else {
            this.show = show
        }
    }

    override fun addFinishHandler(finish: Runnable) {
        finishHandlers.add(finish)
    }

    override fun start() {
        this.task = Bukkit.getScheduler().runTaskTimer(InvUI.getInstance().plugin, Runnable {
            this.handleFrame(this.frame)
            ++this.frame
        }, 0L, tickDelay.toLong())
    }

    override fun cancel() {
        task!!.cancel()
    }

    protected fun finish() {
        task!!.cancel()
        finishHandlers.forEach(Consumer { obj: Runnable? -> obj!!.run() })
    }

    protected abstract fun handleFrame(var1: Int)

    fun getSlots(): CopyOnWriteArrayList<Int?>? {
        return this.slots
    }

    override fun setSlots(slots: List<Int?>?) {
        this.slots = CopyOnWriteArrayList<Int?>(slots)
    }

    protected fun show(vararg slots: Int) {
        for (i in slots) {
            show!!.accept(this.frame, i)
        }
    }

    protected fun convToIndex(x: Int, y: Int): Int {
        if (x < this.width && y < this.height) {
            return SlotUtils.convertToIndex(x, y, this.width)
        } else {
            throw IllegalArgumentException("Coordinates out of bounds")
        }
    }

    protected fun getWidth(): Int {
        return this.width
    }

    protected fun getHeight(): Int {
        return this.height
    }

    fun getCurrentViewers(): MutableSet<Player?>? {
        return windows!!.stream().map { obj: Window -> obj.currentViewer }
            .filter { obj: Player? -> Objects.nonNull(obj) }.collect(
                Collectors.toSet()
            )
    }

}