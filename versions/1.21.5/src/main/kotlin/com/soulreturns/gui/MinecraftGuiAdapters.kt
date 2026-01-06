package com.soulreturns.gui

import com.soulreturns.gui.lib.GuiInteractionHandler
import com.soulreturns.gui.lib.GuiLayoutManager
import com.soulreturns.gui.lib.GuiRenderContext
import com.soulreturns.gui.lib.GuiRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack

/**
 * Minecraft/Fabric-specific implementation of GuiRenderContext.
 *
 * This variant is compiled against Minecraft 1.21.5 mappings, where
 * MatrixStack still exposes push()/pop() and scale(x, y, z).
 */
class MinecraftGuiRenderContext(
    private val context: DrawContext,
    private val client: MinecraftClient,
) : GuiRenderContext {

    override val screenWidth: Int
        get() = client.window.scaledWidth

    override val screenHeight: Int
        get() = client.window.scaledHeight

    override fun drawText(text: String, x: Int, y: Int, color: Int, shadow: Boolean) {
        val renderer = client.textRenderer
        if (shadow) {
            context.drawTextWithShadow(renderer, text, x, y, color)
        } else {
            context.drawText(renderer, text, x, y, color, false)
        }
    }

    override fun drawScaledText(
        text: String,
        x: Int,
        y: Int,
        color: Int,
        shadow: Boolean,
        scale: Float,
    ) {
        if (scale == 1.0f) {
            drawText(text, x, y, color, shadow)
            return
        }

        val renderer = client.textRenderer
        val matrices = context.matrices

        // Convert target top-left coordinates into scaled space.
        val invScale = 1.0f / scale
        val sx = x * invScale
        val sy = y * invScale

        matrices.push()
        matrices.scale(scale, scale, 1.0f)
        if (shadow) {
            context.drawTextWithShadow(renderer, text, sx.toInt(), sy.toInt(), color)
        } else {
            context.drawText(renderer, text, sx.toInt(), sy.toInt(), color, false)
        }
        matrices.pop()
    }

    override fun fillRect(x: Int, y: Int, width: Int, height: Int, color: Int) {
        context.fill(x, y, x + width, y + height, color)
    }

    override fun drawItemIcon(iconKey: String, x: Int, y: Int) {
        // For now, rely on the host to supply a mapping from iconKey to
        // ItemStack via a simple registry. To keep this adapter self-contained,
        // fall back to an empty stack when not found.
        val stack: ItemStack = GuiIconRegistry.resolve(iconKey)
        context.drawItem(stack, x, y)
    }
}

/**
 * Simple registry mapping icon keys to ItemStacks. Hosts can register mappings
 * during mod initialization.
 */
object GuiIconRegistry {
    private val icons: MutableMap<String, ItemStack> = mutableMapOf()

    fun registerIcon(key: String, stack: ItemStack) {
        icons[key] = stack
    }

    fun resolve(key: String): ItemStack {
        return icons[key] ?: ItemStack.EMPTY
    }
}

/**
 * HUD adapter entrypoint called from the InGameHud mixin.
 */
object SoulGuiHudAdapter {
    // Last interaction snapshot from the previous render. This is used by
    // click handling when the user interacts with tracker +/- buttons.
    @Volatile
    var lastSnapshot: com.soulreturns.gui.lib.GuiInteractionSnapshot? = null
        private set

    fun renderHud(context: DrawContext) {
        val client = MinecraftClient.getInstance()
        val layout = GuiLayoutManager.getLayout()
        val guiCtx = MinecraftGuiRenderContext(context, client)
        lastSnapshot = GuiRenderer.renderHud(layout, guiCtx)
    }

    /**
     * Handle a mouse click routed from client code or a mixin.
     */
    fun handleClick(screenX: Int, screenY: Int): Boolean {
        val snapshot = lastSnapshot ?: return false
        return GuiInteractionHandler.handleClick(screenX, screenY, snapshot)
    }
}
