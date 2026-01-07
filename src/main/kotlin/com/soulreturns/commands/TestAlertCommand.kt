package com.soulreturns.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.soulreturns.util.DebugLogger
import com.soulreturns.util.RenderUtils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object TestAlertCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            registerCommand(dispatcher)
        }
    }

    private fun registerCommand(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommandManager.literal("testalert")
                .then(
                    ClientCommandManager.argument("message", StringArgumentType.greedyString())
                        .executes { context ->
                            DebugLogger.logCommandExecution(context.input)
                            val message = StringArgumentType.getString(context, "message")
                            RenderUtils.showAlert(message, 0xFFFF0000.toInt(), 2.0f, 5000) // Show for 5 seconds at 2x size

                            val player = MinecraftClient.getInstance().player
                            val feedback = "§aShowing alert: §r$message"
                            player?.sendMessage(Text.literal(feedback), false)
                            DebugLogger.logSentMessage(feedback)
                            1
                        }
                )
                .executes { context ->
                    DebugLogger.logCommandExecution(context.input)
                    // Default test message
                    RenderUtils.showAlert("Don Expresso is leaving in 1 minute!", 0xFFFF0000.toInt(), 2.0f, 5000)

                    val player = MinecraftClient.getInstance().player
                    val feedback = "§aShowing Don Expresso alert!"
                    player?.sendMessage(Text.literal(feedback), false)
                    DebugLogger.logSentMessage(feedback)
                    1
                }
        )

        dispatcher.register(
            ClientCommandManager.literal("clearalerts")
                .executes { context ->
                    DebugLogger.logCommandExecution(context.input)
                    RenderUtils.clearAlerts()

                    val player = MinecraftClient.getInstance().player
                    val feedback = "§aCleared all alerts!"
                    player?.sendMessage(Text.literal(feedback), false)
                    DebugLogger.logSentMessage(feedback)
                    1
                }
        )
    }
}

