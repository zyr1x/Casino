package ru.lewis.casino.model

import ru.lewis.casino.configuration.type.Dealer
import ru.lewis.casino.configuration.type.ItemTemplate
import ru.lewis.casino.model.jackpot.GameSession
import ru.lewis.casino.model.jackpot.Table
import ru.lewis.casino.model.jackpot.animation.SpinAnimation
import ru.lewis.casino.model.jackpot.impl.GameSessionImpl
import ru.lewis.casino.model.jackpot.impl.TableImpl
import ru.lewis.casino.model.jackpot.menu.AnimatedMenu
import ru.lewis.casino.model.jackpot.menu.DefaultMenu
import ru.lewis.casino.model.menu.button.PlayerButton

interface AssistedInjectFactories {

    fun createTable(dealer: Dealer): TableImpl
    fun createGameSession(table: Table): GameSessionImpl
    fun createPlayerButton(template: ItemTemplate, participant: Participant): PlayerButton
    fun createDefaultMenu(gameSession: GameSession): DefaultMenu
    fun createAnimatedMenu(gameSession: GameSession, winner: Participant): AnimatedMenu
    fun createSpinAnimation(gameSession: GameSession, winner: Participant): SpinAnimation
}