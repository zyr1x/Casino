package ru.lewis.casino.model.hologram

import eu.decentsoftware.holograms.api.holograms.Hologram
import eu.decentsoftware.holograms.api.holograms.HologramLine
import eu.decentsoftware.holograms.api.holograms.HologramPage
import eu.decentsoftware.holograms.api.utils.config.FileConfig
import org.bukkit.Location

class MyHologram : Hologram {
    constructor(name: String, location: Location) :
            super(name, location)

    constructor(name: String, location: Location, saveToFile: Boolean) :
            super(name, location, saveToFile)

    override fun delete() {
        super.delete()
        this.pages.forEach { page: HologramPage? ->
            page?.lines?.forEach { line: HologramLine? ->
                if (line is UpdatableHologramLine) {
                    line.close()
                }
            }
        }
    }
}