package com.soulreturns.mixin.render;

import com.soulreturns.util.DebugLogger;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to log all player-entered chat input (including commands) when debug logging is enabled.
 */
@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(method = "sendMessage", at = @At("HEAD"))
    private void soulmod$logChatInput(String chatText, boolean addToHistory, CallbackInfo ci) {
        DebugLogger.INSTANCE.logChatInput(chatText);
    }
}
