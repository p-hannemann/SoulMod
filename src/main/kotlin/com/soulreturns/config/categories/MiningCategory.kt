package com.soulreturns.config.categories

import com.google.gson.annotations.Expose
import com.soulreturns.config.categories.mining.DwarvenMinesSubCategory
import io.github.notenoughupdates.moulconfig.annotations.Category

class MiningCategory {
    @Expose
    @JvmField
    @Category(name = "Dwarven Mines", desc = "Dwarven Mines related features")
    var dwarvenMinesSubCategory = DwarvenMinesSubCategory()
}