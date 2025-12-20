package ru.lewis.casino.hook

import com.google.inject.Inject
import com.google.inject.Singleton
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import ru.lewis.casino.model.SpinThread

@Singleton
class PlaceholderHook @Inject constructor(
    private val spinThread: SpinThread
) : PlaceholderExpansion() {
    override fun getIdentifier(): String = "casino"

    override fun getAuthor(): String = "Lewis Carrol"

    override fun getVersion(): String = "2.0"

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        if (params.contentEquals("time_remaining")) {
            return spinThread.formatRemaining(spinThread.remaining)
        }
        return "Placeholder is not exists!"
    }
}