package com.soulreturns.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object TestDoubleHookCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("testdh")
                    .executes { context ->
                        runAllTests(context)
                    }
                    .then(
                        ClientCommandManager.argument("testNum", StringArgumentType.word())
                            .executes { context ->
                                val testNum = StringArgumentType.getString(context, "testNum")
                                runSpecificTest(context, testNum)
                            }
                    )
            )
        }
    }

    private fun runAllTests(context: CommandContext<FabricClientCommandSource>): Int {
        val player = context.source.player

        player.sendMessage(Text.literal("§6=== Testing Double Hook Detection ==="), false)
        player.sendMessage(Text.literal("§7Running all test cases..."), false)

        val testCases = getTestCases()
        testCases.forEachIndexed { index, (description, message, shouldTrigger) ->
            val result = if (shouldTrigger) "§a✓ SHOULD TRIGGER" else "§c✗ SHOULD IGNORE"
            player.sendMessage(Text.literal("§e[${index + 1}] $result"), false)
            player.sendMessage(Text.literal("§7$description"), false)
            player.sendMessage(Text.literal("§f\"$message\""), false)

            // Simulate the message
            MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message))
        }

        player.sendMessage(Text.literal("§6=== Test Complete ==="), false)
        player.sendMessage(Text.literal("§7Use §e/testdh <1-${testCases.size}> §7to test individual cases"), false)

        return 1
    }

    private fun runSpecificTest(context: CommandContext<FabricClientCommandSource>, testNum: String): Int {
        val player = context.source.player
        val testCases = getTestCases()

        val index = testNum.toIntOrNull()?.minus(1)
        if (index == null || index !in testCases.indices) {
            player.sendMessage(Text.literal("§cInvalid test number. Use 1-${testCases.size}"), false)
            return 0
        }

        val (description, message, shouldTrigger) = testCases[index]
        val result = if (shouldTrigger) "§a✓ SHOULD TRIGGER" else "§c✗ SHOULD IGNORE"

        player.sendMessage(Text.literal("§6=== Test Case ${index + 1} ==="), false)
        player.sendMessage(Text.literal("$result"), false)
        player.sendMessage(Text.literal("§7$description"), false)
        player.sendMessage(Text.literal("§f\"$message\""), false)

        // Simulate the message
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message))

        return 1
    }

    private fun getTestCases(): List<Triple<String, String, Boolean>> {
        return listOf(
            // Server messages (SHOULD TRIGGER)
            Triple(
                "Pure server message",
                "It's a Double Hook! Woot woot!",
                true
            ),
            Triple(
                "Server message with colors",
                "§6§lDOUBLE HOOK! §eYou got lucky!",
                true
            ),
            Triple(
                "Server announcement style",
                "⚡ Double Hook! ⚡",
                true
            ),
            Triple(
                "Server with prefix",
                "[EVENT] Double Hook! Special catch!",
                true
            ),

            // Player messages in party chat (SHOULD NOT TRIGGER)
            Triple(
                "Party chat with rank",
                "§9Party §8> §6[MVP§3++§6] SoulReturns§f: Double Hook!",
                false
            ),
            Triple(
                "Party chat simple",
                "Party > [MVP++] TestPlayer: Double Hook!",
                false
            ),
            Triple(
                "Party chat no rank",
                "Party > SoulReturns: Double Hook!",
                false
            ),

            // Player messages in public chat (SHOULD NOT TRIGGER)
            Triple(
                "Public chat with lobby number",
                "[505] ⚜ [MVP++] SoulReturns: Double Hook!",
                false
            ),
            Triple(
                "Public chat VIP rank",
                "[VIP+] PlayerName: Double Hook!",
                false
            ),
            Triple(
                "Public chat MVP rank",
                "[MVP+] AnotherPlayer: Double Hook! Woot Woot!",
                false
            ),

            // Guild/Officer chat (SHOULD NOT TRIGGER)
            Triple(
                "Guild chat",
                "Guild > [MVP++] GuildMember: Double Hook!",
                false
            ),
            Triple(
                "Officer chat",
                "Officer > [MVP++] OfficerName: Double Hook!",
                false
            ),

            // Edge cases
            Triple(
                "Player without rank (username:message)",
                "SomePlayer: Double Hook!",
                false
            ),
            Triple(
                "MOD rank",
                "[MOD] ModPlayer: Double Hook!",
                false
            ),
            Triple(
                "ADMIN rank",
                "[ADMIN] AdminPlayer: Double Hook!",
                false
            ),
            Triple(
                "Server message without 'Double Hook'",
                "You caught a fish!",
                false // Won't trigger because no "Double Hook!" text
            )
        )
    }
}

