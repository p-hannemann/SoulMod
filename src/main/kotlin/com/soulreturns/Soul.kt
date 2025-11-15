package com.soulreturns

import com.soulreturns.command.SoulCommand
import com.soulreturns.config.ConfigManager
import net.fabricmc.api.ClientModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Soul : ClientModInitializer {
    private val logger = LoggerFactory.getLogger("soul")

	override fun onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

        // Access config to trigger initialization
//        ConfigManager.config

        SoulCommand.register()
	}

    fun getLogger(): Logger? {
        return this.logger
    }
}