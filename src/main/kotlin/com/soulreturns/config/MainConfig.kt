package com.soulreturns.config

import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.common.text.StructuredText

class MainConfig : Config() {
    override fun getTitle(): StructuredText {
        return StructuredText.of("SoulReturns").green()
    }
}