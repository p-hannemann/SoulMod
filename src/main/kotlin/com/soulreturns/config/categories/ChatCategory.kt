package com.soulreturns.config.categories

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption


class ChatCategory {
    @Expose
    @JvmField
    @Accordion
    @ConfigOption(name = "Double Hook Message", desc = "Message to send when 'Double Hook!' is detected in chat")
    var doubleHookMessage = DoubleHookAccordion()

    class DoubleHookAccordion {
        @Expose
        @JvmField
        @ConfigOption(name = "Woot Woot!", desc = "Main Toggle")
        @ConfigEditorBoolean
        var doubleHookMessageToggle: Boolean = true

        @Expose
        @JvmField
        @ConfigOption(name = "Double Hook Message", desc = "Message to send when 'Double Hook!' is detected in chat")
        @ConfigEditorText
        var doubleHookMessageText: String = "Woot Woot!"
    }
}