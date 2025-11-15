package com.soulreturns.config

import com.soulreturns.Soul
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import java.io.File

object ConfigManager {
    init {
        val config: ManagedConfig<MainConfig>
        Soul.getLogger()?.info("ConfigManager init")

        val configFile = File("config/soul/config.json")
        // Ensure the parent directory exists
        configFile.parentFile?.mkdirs()

//        ManagedConfig.create(configFile, MainConfig::class.java)
    }
}