package ru.lewis.casino.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.configuration.type.MiniMessageComponent
import ru.lewis.casino.extension.asMiniMessageComponent

@ConfigSerializable
data class LocalizationConfiguration(
    val messages: Messages = Messages(),
    val hologram: List<MiniMessageComponent> = listOf(
        "До прокрута:".asMiniMessageComponent(),
        "<time>".asMiniMessageComponent()
    )
) {
    @ConfigSerializable
    data class Messages(
        val errors: Errors = Errors(),
        val notifications: Notifications = Notifications()
    ) {
        @ConfigSerializable
        data class Errors(
            val doubleBet: MiniMessageComponent = "<red>Максимум можно сделать одну ставку!".asMiniMessageComponent(),
            val onlyNumbers: MiniMessageComponent = "<red>Разрешено использовать только числа!".asMiniMessageComponent(),
            val notMoney: MiniMessageComponent = "<red>У вас недостаточно монеток".asMiniMessageComponent()
        )
        @ConfigSerializable
        data class Notifications(
            val doBet: MiniMessageComponent = "Напишите в чат суммку, которую вы хотите поставить на <multiplier>".asMiniMessageComponent(),
            val successfullyBet: MiniMessageComponent = "<green>Вы сделали ставку на <multiplier> в размере <amount>".asMiniMessageComponent(),
            val betWin: MiniMessageComponent = "<green>Ваша ставка зашла, вы получили <amount>".asMiniMessageComponent(),
            val betLoss: MiniMessageComponent = "<red>Вы все проиграли :(".asMiniMessageComponent()
        )
    }
}