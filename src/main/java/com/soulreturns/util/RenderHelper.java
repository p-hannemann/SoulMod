package com.soulreturns.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.joml.Matrix3x2fStack;

public class RenderHelper {
    /**
     * Draw scaled text with shadow at the center of the screen
     * This method properly handles Matrix operations for Minecraft 1.21
     */
    public static void drawScaledText(DrawContext context, TextRenderer textRenderer,
                                     Text text, int centerX, int centerY,
                                     float scale, int color) {
        Matrix3x2fStack matrices = context.getMatrices();

        int textWidth = textRenderer.getWidth(text);

        // Calculate scaled dimensions
        int scaledTextWidth = (int)(textWidth * scale);

        // Calculate position in scaled coordinate space
        float x = (centerX - scaledTextWidth / 2.0f) / scale;
        float y = centerY / scale;

        // Save matrix state and apply scaling
        matrices.pushMatrix();
        matrices.scale(scale, scale);

        // Draw text at the scaled position
        context.drawTextWithShadow(textRenderer, text, (int)x, (int)y, color);

        // Restore matrix state
        matrices.popMatrix();
    }
}

