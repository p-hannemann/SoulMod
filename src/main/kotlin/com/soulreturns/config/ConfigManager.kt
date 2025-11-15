package com.soulreturns.config

import com.soulreturns.Soul
import com.soulreturns.Soul.config
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import java.io.File

class ConfigManager {
    init {
        val configFile = File("config/test/config.json")

        config = ManagedConfig.create(configFile, MainConfig::class.java)

        // Ensure the parent directory exists
        configFile.parentFile?.mkdirs()
    }
}