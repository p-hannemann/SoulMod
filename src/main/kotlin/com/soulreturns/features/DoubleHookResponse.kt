package com.soulreturns.features

import com.soulreturns.Soul
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.MinecraftClient

object DoubleHookResponse {
    private var lastProcessed: String? = null

    fun register() {
        // Register for normal chat messages (newer Fabric: 5 parameters)
        ClientReceiveMessageEvents.CHAT.register { message, _, _, _, _ ->
            handleMessage(message.string)
        }
        // Register for game/system messages (some servers push kill feeds or ability procs here)
        ClientReceiveMessageEvents.GAME.register { message, overlay ->
            if (overlay) return@register // Ignore action bar / overlay lines
            handleMessage(message.string)
        }
        Soul.getLogger()?.info("DoubleHookResponse listeners registered")
    }

    private fun handleMessage(raw: String) {
        try {
            // Feature toggle
            if (!Soul.configManager.config.instance.ChatCategory.doubleHookMessage.doubleHookMessageToggle) return

            val player = MinecraftClient.getInstance().player ?: return

            // Basic deduplication in case both GAME and CHAT fire for same content
            val trimmed = raw.trim()
            if (lastProcessed == trimmed) return else lastProcessed = trimmed

            // Only trigger on server messages, not player messages
            if (isPlayerMessage(trimmed)) {
                Soul.getLogger()?.debug("Ignoring player message: $trimmed")
                return
            }

            if (trimmed.contains("Double Hook!", ignoreCase = true)) {
                Soul.getLogger()?.info("Detected 'Double Hook!' in server message, sending party cheer")
                player.networkHandler.sendChatCommand("pc " + Soul.configManager.config.instance.ChatCategory.doubleHookMessage.doubleHookMessageText)
            }
        } catch (t: Throwable) {
            Soul.getLogger()?.error("Error handling chat message for DoubleHookResponse", t)
        }
    }

    /**
     * Detects if a message is from a player rather than the server.
     * Player messages typically contain:
     * - Rank prefixes like [MVP++], [VIP], etc. (with or without color codes)
     * - Party chat prefix: "Party >"
     * - Guild chat prefix: "Guild >"
     * - Colon followed by player message (e.g., "PlayerName: message")
     * - Player name patterns with ranks
     */
    private fun isPlayerMessage(message: String): Boolean {
        // Strip Minecraft color codes for easier pattern matching
        val stripped = message.replace("§[0-9a-fk-or]".toRegex(), "")

        // Check for common player message patterns
        return when {
            // Party chat: "Party > [RANK] PlayerName: message"
            stripped.contains("Party >", ignoreCase = false) -> true

            // Guild chat: "Guild > [RANK] PlayerName: message"
            stripped.contains("Guild >", ignoreCase = false) -> true

            // Officer chat: "Officer >"
            stripped.contains("Officer >", ignoreCase = false) -> true

            // Rank patterns like [MVP++], [VIP], [VIP+], etc.
            // Followed by a colon (indicating player sent message)
            stripped.matches(".*\\[(VIP|MVP|MOD|ADMIN|HELPER|YOUTUBE|PIG).*?].*:.*".toRegex(RegexOption.IGNORE_CASE)) -> true

            // Lobby number pattern like "[505]" followed by rank and colon
            stripped.matches(".*\\[\\d+].*\\[.*?].*:.*".toRegex()) -> true

            // Generic pattern: looks like "SomeName: message" (name followed by colon)
            // But exclude if it's clearly a server announcement (e.g., starts with special chars)
            stripped.matches(".*[a-zA-Z0-9_]{3,16}\\s*:.*".toRegex()) && !stripped.matches("^[^a-zA-Z0-9]*(?:DOUBLE|TRIPLE|RARE|LEGENDARY|SPECIAL|EVENT).*".toRegex(RegexOption.IGNORE_CASE)) -> true

            else -> false
        }
    }
}
