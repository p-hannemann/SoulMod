package com.soulreturns.config

import com.google.gson.annotations.Expose
import com.soulreturns.Soul
import com.soulreturns.config.categories.RenderCategory
import com.soulreturns.config.categories.FishingCategory
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.common.text.StructuredText

class MainConfig : Config() {
    override fun getTitle(): StructuredText {
        return StructuredText.of("Soul Mod - v${Soul.version}").green()
    }

    @JvmField
    @Expose
    @Category(name = "Render", desc = "Render features")
    var RenderCategory = RenderCategory()

    @JvmField
    @Expose
    @Category(name = "Fishing", desc = "Fishing features")
    var FishingCategory = FishingCategory()
}