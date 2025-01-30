package ru.lewis.casino.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import ru.lewis.casino.configuration.type.MiniMessageComponent
import ru.lewis.casino.extension.asMiniMessageComponent

@ConfigSerializable
data class MessagesConfiguration(
    val common: CommonMessages = CommonMessages(),
    val errors: Errors = Errors(),
    )
{

    @ConfigSerializable
    data class CommonMessages(
        val gameEnd: MiniMessageComponent = "Игра закончена, подебил <player>, он забирает <money> с шансом <percent>".asMiniMessageComponent(),
        val gameStart: MiniMessageComponent = "Скоро начнутся розыгрыши на столе <table>, успей и ты поставить ставку!".asMiniMessageComponent(),
        val gameStartAnimation: MiniMessageComponent = "Розыгрыши на стоне <table> уже начались!".asMiniMessageComponent(),
        val bet: MiniMessageComponent = "Вы успешно поставили ставку в размере <bet>".asMiniMessageComponent()
    )

    @ConfigSerializable
    data class Errors(
        val minBetErrors: MiniMessageComponent = "Минимальная ставка это <min-bet>".asMiniMessageComponent(),
        val maxBetErrors: MiniMessageComponent = "Максимальная ставка это <max-bet>".asMiniMessageComponent(),
        val noMoney: MiniMessageComponent = " У вас недостаточно денег для ставки".asMiniMessageComponent(),
        val rouletteIsStart: MiniMessageComponent = "Игра начата дилер не принимает ставки!".asMiniMessageComponent(),
        val repeatBet: MiniMessageComponent = "нихуя себе ты захотел, а нахуй не сходишь?".asMiniMessageComponent()
    )
}
