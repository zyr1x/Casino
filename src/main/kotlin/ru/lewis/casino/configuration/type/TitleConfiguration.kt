package ru.lewis.casino.configuration.type

import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.time.Duration

@ConfigSerializable
data class TitleConfiguration(
    val title: MiniMessageComponent,
    val subtitle: MiniMessageComponent,
    val durationFadeIn: Duration = Ticks.duration(10),
    val durationStay: Duration = Ticks.duration(70),
    val durationFadeOut: Duration = Ticks.duration(20),
) {

    fun createTitle(): Title = Title.title(
        title.asComponent(),
        subtitle.asComponent(),
        Title.Times.times(durationFadeIn, durationStay, durationFadeOut)
    )
}
