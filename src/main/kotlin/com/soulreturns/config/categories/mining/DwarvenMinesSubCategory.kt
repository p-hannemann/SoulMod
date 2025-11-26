package com.soulreturns.config.categories.mining

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DwarvenMinesSubCategory {
    @Expose
    @JvmField
    @ConfigOption(name = "Don Expresso", desc = "Show an alert when Don Expresso is about to leave")
    @ConfigEditorBoolean
    var donExpressoAlert: Boolean = true
}