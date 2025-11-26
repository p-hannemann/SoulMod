package com.soulreturns.commands.subcommands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.soulreturns.util.MessageHandler
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object TestMessageSubcommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource> {
        return ClientCommandManager.literal("testmessage")
            .then(
                ClientCommandManager.literal("server")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.SERVER, message, false)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("serverColor")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.SERVER, message, true)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("party")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.PARTY, message, false)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("partyColor")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.PARTY, message, true)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("public")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.PUBLIC, message, false)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("publicColor")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.PUBLIC, message, true)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("guild")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.GUILD, message, false)
                                1
                            }
                    )
            )
            .then(
                ClientCommandManager.literal("guildColor")
                    .then(
                        ClientCommandManager.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val message = StringArgumentType.getString(context, "message")
                                sendTestMessage(MessageType.GUILD, message, true)
                                1
                            }
                    )
            )
    }

    private enum class MessageType {
        SERVER, PARTY, PUBLIC, GUILD
    }

    private fun sendTestMessage(type: MessageType, userMessage: String, withColor: Boolean) {
        val formattedMessage = when (type) {
            MessageType.SERVER -> {
                if (withColor) {
                    "§e$userMessage"
                } else {
                    userMessage
                }
            }
            MessageType.PARTY -> {
                if (withColor) {
                    "§9Party §8> §6[MVP§3++§6] TestPlayer§f: $userMessage"
                } else {
                    "Party > [MVP++] TestPlayer: $userMessage"
                }
            }
            MessageType.PUBLIC -> {
                if (withColor) {
                    "§7[§a123§7] §6[MVP§c++§6] TestPlayer§f: $userMessage"
                } else {
                    "[123] [MVP++] TestPlayer: $userMessage"
                }
            }
            MessageType.GUILD -> {
                if (withColor) {
                    "§2Guild §8> §6[MVP§3++§6] TestPlayer§f: $userMessage"
                } else {
                    "Guild > [MVP++] TestPlayer: $userMessage"
                }
            }
        }

        // Send the formatted message to chat for visibility
        val minecraft = MinecraftClient.getInstance()
        minecraft.inGameHud.chatHud.addMessage(Text.literal(formattedMessage))

        // Process the message through MessageHandler to trigger handlers
        MessageHandler.simulateMessage(formattedMessage)
    }
}

