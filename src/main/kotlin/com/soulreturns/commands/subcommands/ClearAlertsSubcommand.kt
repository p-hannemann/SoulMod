package com.soulreturns.commands.subcommands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.soulreturns.util.DebugLogger
import com.soulreturns.util.RenderUtils
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object ClearAlertsSubcommand : SoulSubcommand {

    override fun register(): LiteralArgumentBuilder<FabricClientCommandSource> {
        return literal("clearalerts") {
            runs { _ ->
                RenderUtils.clearAlerts()
                val feedback = "§aCleared all alerts!"
                MinecraftClient.getInstance().player?.sendMessage(Text.literal(feedback), false)
                DebugLogger.logSentMessage(feedback)
            }
        }
    }
}

