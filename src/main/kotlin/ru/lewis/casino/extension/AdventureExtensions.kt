package ru.lewis.casino.extension

import io.github.blackbaroness.durationserializer.DurationFormats
import io.github.blackbaroness.durationserializer.DurationSerializer
import io.github.blackbaroness.durationserializer.format.DurationFormat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.TagPattern
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.util.Ticks
import org.bukkit.Color
import ru.lewis.casino.configuration.type.MiniMessageComponent
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.Locale

fun ComponentLike.legacy(): String {
    return LegacyComponentSerializer.legacySection().serialize(asComponent())
}

fun number(@TagPattern key: String, number: Number?): TagResolver {
    val n = number?.toDouble() ?: 0.0
    return TagResolver.resolver(key) { argumentQueue, context ->
        val decimalFormat = if (n % 1.0 == 0.0) {
            // целое число
            val symbols = DecimalFormatSymbols(Locale.US)
            symbols.groupingSeparator = '.'
            DecimalFormat("#,###", symbols)
        } else {
            // дробное число
            val symbols = DecimalFormatSymbols(Locale.US)
            symbols.groupingSeparator = '.'
            symbols.decimalSeparator = ','
            DecimalFormat("#,###.##", symbols)
        }

        Tag.inserting(context.deserialize(decimalFormat.format(n)))
    }
}

fun String.asMiniMessageComponent(): MiniMessageComponent {
    return MiniMessageComponent(this, this.parseMiniMessage())
}

fun Component.asMiniMessageComponent(): MiniMessageComponent {
    return MiniMessageComponent(
        MiniMessage.miniMessage().serialize(this)
            .removePrefix("<!italic><!underlined><!strikethrough><!bold><!obfuscated>"), this
    )
}

fun String.parseMiniMessage(vararg tagResolvers: TagResolver): Component =
    MiniMessage.miniMessage().deserialize(this, *tagResolvers)

fun Duration.format(
    unit: ChronoUnit = ChronoUnit.MINUTES,
    format: DurationFormat = DurationFormats.mediumLengthRussian(),
    avoidZero: Boolean = true
) = truncate(unit, avoidZero).format(format)

fun Throwable.rootCause(): Throwable {
    var cause = this.cause ?: return this

    while (true) {
        cause = cause.cause ?: return cause
    }
}

fun Duration.truncate(unit: ChronoUnit, avoidZero: Boolean = true): Duration {
    val duration = truncatedTo(unit)
    if (avoidZero && duration < unit.duration) {
        return duration.plus(unit.duration)
    }

    return duration
}

fun Duration.format(durationFormat: DurationFormat): String {
    return DurationSerializer.serialize(this.withNanos(0), durationFormat)
}

val kotlin.time.Duration.inTicks: Long
    get() {
        return this.inWholeMilliseconds / Ticks.SINGLE_TICK_DURATION_MS
    }

fun Color.toAwtColor(): java.awt.Color {
    return java.awt.Color(this.red, this.green, this.blue)
}
