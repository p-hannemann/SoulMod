package com.soulreturns.command

import com.mojang.brigadier.context.CommandContext
import com.soulreturns.Soul
import com.soulreturns.commands.subcommands.ClearAlertsSubcommand
import com.soulreturns.commands.subcommands.GuiSubcommand
import com.soulreturns.commands.subcommands.TestAlertSubcommand
import com.soulreturns.commands.subcommands.TestMessageSubcommand
import com.soulreturns.config.lib.ui.ModConfigScreen
import com.soulreturns.util.DebugLogger
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object SoulCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(
                ClientCommandManager.literal("soul")
                    .executes { context ->
                        DebugLogger.logCommandExecution(context.input)
                        execute(context)
                    }
                    .then(TestMessageSubcommand.register())
                    .then(TestAlertSubcommand.register())
                    .then(ClearAlertsSubcommand.register())
                    .then(GuiSubcommand.register())
            )
        }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(
                ClientCommandManager.literal("test")
                    .executes { context ->
                        DebugLogger.logCommandExecution(context.input)
                        val test = Soul.configManager.config.instance.renderCategory.hideHeldItemTooltip
                        val message = "Setting: $test"
                        context.source.player.sendMessage(Text.literal(message), false)
                        DebugLogger.logSentMessage(message)
                        0
                    }
            )
        }
    }

    private fun execute(context: CommandContext<FabricClientCommandSource>): Int {
        MinecraftClient.getInstance().send {
            val screen = ModConfigScreen(
                Soul.configManager.config,
                "Soul Mod",
                Soul.version
            )
            MinecraftClient.getInstance().setScreen(screen)
        }

        return 1 // Success
    }
}
