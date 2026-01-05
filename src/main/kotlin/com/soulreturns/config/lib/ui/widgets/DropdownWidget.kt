package com.soulreturns.config.lib.ui.widgets

import com.soulreturns.config.lib.model.OptionData
import com.soulreturns.config.lib.ui.RenderHelper
import com.soulreturns.util.DebugLogger
import net.minecraft.client.gui.DrawContext

/**
 * Dropdown selection widget
 */
class DropdownWidget(
    option: OptionData,
    x: Int,
    y: Int,
    private val values: Array<String>
) : ConfigWidget(option, x, y, 400, 30) {
    
    private var isExpanded = false
    private val dropdownHeight = 24
    private val itemHeight = 20
    
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float, configInstance: Any) {
        val currentValue = getValue(configInstance) as? String ?: values.firstOrNull() ?: ""
        val textRenderer = net.minecraft.client.MinecraftClient.getInstance().textRenderer
        
        // Draw option name
        context.drawText(textRenderer, option.name, x, y + 7, 0xFFFFFFFF.toInt(), false)
        
        // Dropdown button
        val dropdownX = x + width - 200
        val dropdownY = y
        
        // Check if hovering over dropdown button
        val isDropdownHovered = mouseX >= dropdownX && mouseX <= dropdownX + 200 &&
                                mouseY >= dropdownY && mouseY <= dropdownY + dropdownHeight
        
        val bgColor = if (isDropdownHovered) 0xFF3C3C3C.toInt() else 0xFF2C2C2C.toInt()
        RenderHelper.drawRoundedRect(context, dropdownX, dropdownY, 200, dropdownHeight, 4f, bgColor)
        
        // Draw current value
        context.drawText(textRenderer, currentValue, dropdownX + 8, dropdownY + 8, 0xFFFFFFFF.toInt(), false)
        
        // Draw arrow
        val arrowX = dropdownX + 185
        val arrowY = dropdownY + 12
        val arrow = if (isExpanded) "▲" else "▼"
        context.drawText(textRenderer, arrow, arrowX, arrowY, 0xFFCCCCCC.toInt(), false)
        
        // Draw dropdown menu if expanded
        if (isExpanded) {
            val menuY = dropdownY + dropdownHeight + 2
            val menuHeight = values.size * itemHeight
            
            // Menu background
            RenderHelper.drawRoundedRect(context, dropdownX, menuY, 200, menuHeight, 4f, 0xFF1C1C1C.toInt())
            
            // Menu items
            for ((index, value) in values.withIndex()) {
                val itemY = menuY + (index * itemHeight)
                val isItemHovered = mouseX >= dropdownX && mouseX <= dropdownX + 200 &&
                                   mouseY >= itemY && mouseY <= itemY + itemHeight
                
                val isSelected = value == currentValue
                val itemBgColor = when {
                    isSelected -> 0xFF4C9AFF.toInt()
                    isItemHovered -> 0xFF3C3C3C.toInt()
                    else -> 0
                }
                
                if (itemBgColor != 0) {
                    context.fill(dropdownX, itemY, dropdownX + 200, itemY + itemHeight, itemBgColor)
                }
                
                context.drawText(textRenderer, value, dropdownX + 8, itemY + 6, 0xFFFFFFFF.toInt(), false)
            }
        }
    }
    
    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int, configInstance: Any): Boolean {
        if (button != 0) return false
        
        val dropdownX = x + width - 200
        val dropdownY = y
        
        // Check if clicking dropdown button
        if (mouseX >= dropdownX && mouseX <= dropdownX + 200 &&
            mouseY >= dropdownY && mouseY <= dropdownY + dropdownHeight) {
            isExpanded = !isExpanded
            return true
        }
        
        // Check if clicking menu items
        if (isExpanded) {
            val menuY = dropdownY + dropdownHeight + 2
            for ((index, value) in values.withIndex()) {
                val itemY = menuY + (index * itemHeight)
                if (mouseX >= dropdownX && mouseX <= dropdownX + 200 &&
                    mouseY >= itemY && mouseY <= itemY + itemHeight) {
                    val oldValue = getValue(configInstance) as? String ?: ""
                    DebugLogger.logWidgetInteraction("Dropdown '${option.name}': $oldValue -> $value")
                    setValue(configInstance, value)
                    isExpanded = false
                    return true
                }
            }
        }
        
        // Close dropdown if clicking outside
        if (isExpanded) {
            isExpanded = false
        }
        
        return false
    }
}
