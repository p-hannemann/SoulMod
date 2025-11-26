package com.soulreturns.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
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
                            val message = StringArgumentType.getString(context, "message")
                            RenderUtils.showAlert(message, 0xFFFF0000.toInt(), 2.0f, 5000) // Show for 5 seconds at 2x size

                            val player = MinecraftClient.getInstance().player
                            player?.sendMessage(Text.literal("§aShowing alert: §r$message"), false)
                            1
                        }
                )
                .executes { _ ->
                    // Default test message
                    RenderUtils.showAlert("Don Expresso is leaving in 1 minute!", 0xFFFF0000.toInt(), 2.0f, 5000)

                    val player = MinecraftClient.getInstance().player
                    player?.sendMessage(Text.literal("§aShowing Don Expresso alert!"), false)
                    1
                }
        )

        dispatcher.register(
            ClientCommandManager.literal("clearalerts")
                .executes { _ ->
                    RenderUtils.clearAlerts()

                    val player = MinecraftClient.getInstance().player
                    player?.sendMessage(Text.literal("§aCleared all alerts!"), false)
                    1
                }
        )
    }
}

