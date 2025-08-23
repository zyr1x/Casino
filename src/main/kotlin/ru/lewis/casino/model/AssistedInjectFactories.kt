package ru.lewis.casino.model

import ru.lewis.casino.configuration.type.Slot
import ru.lewis.casino.model.menu.button.BetButton

interface AssistedInjectFactories {
    fun createBetButton(slot: Slot): BetButton
}