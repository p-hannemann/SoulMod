package com.soulreturns.command

import com.mojang.brigadier.context.CommandContext
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
                        execute(context)
                    }
            )
        }
    }

    private fun execute(context: CommandContext<FabricClientCommandSource>): Int {
        context.source.player.sendMessage(Text.literal("Opening Soul config..."), false)

        MinecraftClient.getInstance().send {
//            val editor = SoulClient.config.getEditor()
//            editor.setWide(false)
//            SoulClient.config.openConfigGui()
        }

        return 1 // Success
    }
}