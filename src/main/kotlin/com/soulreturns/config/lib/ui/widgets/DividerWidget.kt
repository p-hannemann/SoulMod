package com.soulreturns.config.lib.ui.widgets

import com.soulreturns.config.lib.model.OptionData
import com.soulreturns.config.lib.model.OptionType
import com.soulreturns.config.lib.ui.themes.Theme
import net.minecraft.client.gui.DrawContext

/**
 * Divider widget for visual separation between config sections
 */
class DividerWidget(
    option: OptionData,
    x: Int,
    y: Int,
    private val dividerType: OptionType.Divider
) : ConfigWidget(option, x, y, 400, if (dividerType.label.isNotEmpty()) 30 else 15) {
    
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float, configInstance: Any, theme: Theme) {
        val textRenderer = net.minecraft.client.MinecraftClient.getInstance().textRenderer
        
        if (dividerType.label.isNotEmpty()) {
            // Draw label if present
            val labelWidth = textRenderer.getWidth(dividerType.label)
            val labelX = x
            val labelY = y + 5
            
            context.drawText(textRenderer, dividerType.label, labelX, labelY, theme.textSecondary, false)
            
            // Draw line after label
            val lineStartX = labelX + labelWidth + 10
            val lineY = y + height / 2
            val lineEndX = x + width
            val lineHeight = 1
            
            context.fill(lineStartX, lineY, lineEndX, lineY + lineHeight, theme.categoryBorder)
        } else {
            // Draw full-width line
            val lineY = y + height / 2
            val lineHeight = 1
            
            context.fill(x, lineY, x + width, lineY + lineHeight, theme.categoryBorder)
        }
    }
    
    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int, configInstance: Any): Boolean {
        // Dividers don't handle clicks
        return false
    }
}
