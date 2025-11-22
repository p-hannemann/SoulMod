package com.soulreturns.config

import com.google.gson.annotations.Expose
import com.soulreturns.config.categories.RenderCategory
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import io.github.notenoughupdates.moulconfig.common.text.StructuredText

class MainConfig : Config() {
    override fun getTitle(): StructuredText {
        return StructuredText.of("Soul Mod").green()
    }

    @JvmField
    @Expose
    @Category(name = "Render", desc = "Render stuff here")
    var RenderCategory = RenderCategory()
}