package com.soulreturns.config.categories

import com.soulreturns.config.categories.fishing.ChatSubCategory
import com.soulreturns.config.categories.fishing.BobbinTimeSubCategory
import com.soulreturns.config.lib.annotations.ConfigSubcategory

class FishingCategory {
    @JvmField
    @ConfigSubcategory(name = "Chat", description = "Chat features")
    var chatSubCategory = ChatSubCategory()

    @JvmField
    @ConfigSubcategory(name = "Bobbin Time", description = "Fishing bobber counter and alert features")
    var bobbinTimeSubCategory = BobbinTimeSubCategory()
}
