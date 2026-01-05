package com.soulreturns.config.lib.ui

import net.minecraft.client.gui.DrawContext
import kotlin.math.*

/**
 * Helper class for modern UI rendering with rounded corners, gradients, and animations
 */
object RenderHelper {
    
    /**
     * Linear interpolation for smooth animations
     */
    fun lerp(start: Float, end: Float, progress: Float): Float {
        return start + (end - start) * progress
    }
    
    /**
     * Easing function for smooth animations (ease-out cubic)
     */
    fun easeOutCubic(t: Float): Float {
        val f = t - 1f
        return f * f * f + 1f
    }
    
    /**
     * Easing function for smooth animations (ease-in-out cubic)
     */
    fun easeInOutCubic(t: Float): Float {
        return if (t < 0.5f) {
            4f * t * t * t
        } else {
            val f = 2f * t - 2f
            1f + f * f * f / 2f
        }
    }
    
    /**
     * Draws a filled rectangle with rounded corners (anti-aliased)
     */
    fun drawRoundedRect(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Float,
        color: Int
    ) {
        if (radius <= 0) {
            context.fill(x, y, x + width, y + height, color)
            return
        }
        
        val r = radius.coerceAtMost(width / 2f).coerceAtMost(height / 2f)
        
        if (r < 1f) {
            context.fill(x, y, x + width, y + height, color)
            return
        }
        
        // Fill main rectangular areas (no rounding needed)
        val ri = r.toInt()
        context.fill(x + ri, y, x + width - ri, y + height, color) // Center vertical strip
        context.fill(x, y + ri, x + ri, y + height - ri, color) // Left edge
        context.fill(x + width - ri, y + ri, x + width, y + height - ri, color) // Right edge
        
        // Draw anti-aliased corners
        drawSmoothCorner(context, x + ri, y + ri, ri, color, 2) // Top-left
        drawSmoothCorner(context, x + width - ri - 1, y + ri, ri, color, 1) // Top-right
        drawSmoothCorner(context, x + ri, y + height - ri - 1, ri, color, 3) // Bottom-left
        drawSmoothCorner(context, x + width - ri - 1, y + height - ri - 1, ri, color, 0) // Bottom-right
    }
    
    /**
     * Draws a smooth anti-aliased quarter circle for rounded corners
     * quadrant: 0=bottom-right, 1=top-right, 2=top-left, 3=bottom-left
     */
    private fun drawSmoothCorner(
        context: DrawContext,
        cx: Int,
        cy: Int,
        radius: Int,
        color: Int,
        quadrant: Int
    ) {
        val baseAlpha = (color shr 24 and 0xFF)
        val baseRGB = color and 0xFFFFFF
        
        // Expand range for better anti-aliasing (2 pixels for smoother edge)
        val range = radius + 2
        
        for (dy in -range..range) {
            for (dx in -range..range) {
                // Check if in correct quadrant
                val inQuadrant = when (quadrant) {
                    0 -> dx >= 0 && dy >= 0 // bottom-right
                    1 -> dx >= 0 && dy <= 0 // top-right  
                    2 -> dx <= 0 && dy <= 0 // top-left
                    3 -> dx <= 0 && dy >= 0 // bottom-left
                    else -> false
                }
                
                if (!inQuadrant) continue
                
                // Calculate distance from center (sub-pixel accuracy)
                val dist = sqrt((dx * dx + dy * dy).toFloat())
                
                // Multi-sample anti-aliasing for even smoother edges
                var coverage = 0f
                val samples = 4 // 4x MSAA
                val sampleOffset = 0.25f
                
                for (sy in 0 until 2) {
                    for (sx in 0 until 2) {
                        val sampleDist = sqrt(
                            ((dx + sx * sampleOffset - 0.25f) * (dx + sx * sampleOffset - 0.25f) +
                            (dy + sy * sampleOffset - 0.25f) * (dy + sy * sampleOffset - 0.25f))
                        )
                        if (sampleDist <= radius.toFloat()) {
                            coverage += 1f / samples
                        }
                    }
                }
                
                // Apply coverage to alpha
                val alpha = (baseAlpha * coverage).toInt().coerceIn(0, 255)
                
                if (alpha > 0) {
                    val pixelColor = (alpha shl 24) or baseRGB
                    context.fill(cx + dx, cy + dy, cx + dx + 1, cy + dy + 1, pixelColor)
                }
            }
        }
    }
    
    /**
     * Draws a filled rectangle with a vertical gradient (simplified)
     */
    fun drawGradientRect(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        colorTop: Int,
        colorBottom: Int
    ) {
        // Simplified: use fillGradient from DrawContext
        context.fillGradient(x, y, x + width, y + height, colorTop, colorBottom)
    }
    
    /**
     * Draws a shadow/glow effect around a rectangle
     */
    fun drawShadow(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        blur: Int,
        color: Int
    ) {
        for (i in 0 until blur) {
            val alpha = ((color shr 24 and 0xFF) * (1f - i.toFloat() / blur)).toInt()
            val shadowColor = (alpha shl 24) or (color and 0xFFFFFF)
            context.fill(x - i, y - i, x + width + i, y + height + i, shadowColor)
        }
    }
    
    /**
     * Interpolate between two colors
     */
    fun lerpColor(colorStart: Int, colorEnd: Int, progress: Float): Int {
        val alphaStart = (colorStart shr 24 and 0xFF)
        val redStart = (colorStart shr 16 and 0xFF)
        val greenStart = (colorStart shr 8 and 0xFF)
        val blueStart = (colorStart and 0xFF)
        
        val alphaEnd = (colorEnd shr 24 and 0xFF)
        val redEnd = (colorEnd shr 16 and 0xFF)
        val greenEnd = (colorEnd shr 8 and 0xFF)
        val blueEnd = (colorEnd and 0xFF)
        
        val alpha = (alphaStart + (alphaEnd - alphaStart) * progress).toInt()
        val red = (redStart + (redEnd - redStart) * progress).toInt()
        val green = (greenStart + (greenEnd - greenStart) * progress).toInt()
        val blue = (blueStart + (blueEnd - blueStart) * progress).toInt()
        
        return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
    }
    
    /**
     * Draws a rectangle with a border and rounded corners
     */
    fun drawRectWithBorder(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Float,
        fillColor: Int,
        borderColor: Int,
        borderWidth: Int = 1
    ) {
        // Draw border (larger rounded rect)
        if (borderWidth > 0) {
            drawRoundedRect(context, x - borderWidth, y - borderWidth, 
                width + borderWidth * 2, height + borderWidth * 2, radius, borderColor)
        }
        
        // Draw fill on top
        drawRoundedRect(context, x, y, width, height, radius, fillColor)
    }
    
    /**
     * Check if mouse is within bounds
     */
    fun isMouseOver(mouseX: Int, mouseY: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
    }
}
