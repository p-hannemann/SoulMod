package com.soulreturns.mixin.render;

import com.soulreturns.gui.SoulGuiHudAdapter;
import com.soulreturns.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.soulreturns.config.ConfigInstanceKt.getConfig;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(
            method = "renderHeldItemTooltip",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
        if (getConfig().renderCategory.hideHeldItemTooltip) {
            ci.cancel();
        }
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    public void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        // Existing HUD alerts
        RenderUtils.INSTANCE.renderAlerts(context);
        // GUI library-driven HUD elements (text blocks, trackers, etc.)
        SoulGuiHudAdapter.INSTANCE.renderHud(context);
    }
}
