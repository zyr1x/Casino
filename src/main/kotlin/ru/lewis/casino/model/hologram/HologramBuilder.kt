package ru.lewis.casino.model.hologram

import eu.decentsoftware.holograms.api.holograms.HologramPage
import org.bukkit.Location
import java.util.UUID

class HologramBuilder(private val location: Location) {
    private var lines: ((HologramPage) -> List<UpdatableHologramLine>)? = null

    fun setLines(lines: (HologramPage) -> List<UpdatableHologramLine>): HologramBuilder {
        this.lines = lines
        return this
    }

    fun build(): MyHologram {
        val hologram = MyHologram(UUID.randomUUID().toString(), location.toCenterLocation(), false)
        val page = hologram.pages.first() as HologramPage

        lines?.let { createLines ->
            val createdLines = createLines(page)
            createdLines.forEach { page.addLine(it) }
        }

        return hologram
    }
}