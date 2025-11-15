package com.soulreturns.config.categories

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class RenderCategory {
    @Expose
    @JvmField
    @ConfigOption(name = "Testbool", desc = "testbool ig")
    @ConfigEditorBoolean
    var test: Boolean = true;
}