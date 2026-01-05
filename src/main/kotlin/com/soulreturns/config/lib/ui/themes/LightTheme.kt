package com.soulreturns.config.lib.ui.themes

/**
 * Light theme (clean modern look)
 */
object LightTheme : Theme {
    override val name = "Light"
    
    // Background colors
    override val backgroundTop = 0xFFF5F5F5.toInt()
    override val backgroundBottom = 0xFFFFFFFF.toInt()
    override val overlayColor = 0x80000000.toInt()
    
    // Sidebar colors
    override val sidebarBackground = 0xFFF8F8F8.toInt()
    override val categoryBackground = 0xFFFFFFFF.toInt()
    override val categoryHover = 0xFFF0F0F0.toInt()
    override val categorySelected = 0xFFE8E8E8.toInt()
    override val subcategoryBackground = 0xFFF5F5F5.toInt()
    override val subcategoryHover = 0xFFEAEAEA.toInt()
    override val subcategorySelected = 0xFFDDDDDD.toInt()
    
    // Content area colors
    override val contentBackground = 0xFFFFFFFF.toInt()
    
    // Title bar colors
    override val titleBarBackground = 0xFFFFFFFF.toInt()
    override val closeButtonNormal = 0xFFE0E0E0.toInt()
    override val closeButtonHover = 0xFFFF6B6B.toInt()
    
    // Text colors
    override val textPrimary = 0xFF1A1A1A.toInt()
    override val textSecondary = 0xFF666666.toInt()
    
    // Widget colors
    override val widgetBackground = 0xFFF0F0F0.toInt()
    override val widgetHover = 0xFFE5E5E5.toInt()
    override val widgetActive = 0xFF4A90E2.toInt()
}
