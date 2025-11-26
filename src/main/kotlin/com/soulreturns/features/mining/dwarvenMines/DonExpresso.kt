package com.soulreturns.features.mining.dwarvenMines

import com.soulreturns.config.config
import com.soulreturns.util.MessageDetector
import com.soulreturns.util.MessageHandler
import com.soulreturns.util.RenderUtils

object DonExpresso {
    fun register() {
        if (!config.miningCategory.dwarvenMinesSubCategory.donExpressoAlert) return
        MessageHandler.onServerMessage { message ->
            if (MessageDetector.containsPattern(message, "Don Expresso is leaving in 1 minute! Make sure to feed him everything you have before he leaves!")) {
                RenderUtils.showAlert("Don Expresso is leaving in 1 minute!", 0xFFFF0000.toInt(), 4.0f, 5000)
            }
        }
    }
}