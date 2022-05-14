package dev.julixn.ps.test

import dev.julixn.ps.api.events.TestEvent
import dev.julixn.ps.api.events.TestEvent2
import dev.julixn.ps.api.logging.Logger
import dev.julixn.ps.systems.loader.PluginLoader
import dev.julixn.ps.systems.manager.PluginManager

class PluginSystem {

    companion object {
        val logger: Logger = Logger("[PS | %TIME%]", true)
        val pluginManager: PluginManager = PluginManager(logger)
    }

    fun start() {
        logger.log("Application started!", "[MAIN]")
        PluginLoader(logger, pluginManager).load()
        pluginManager.enablePlugins(false)
        pluginManager.sendEvent(TestEvent(1))
        pluginManager.sendEvent(TestEvent2("ev"))
        logger.log("Application stopped!", "[MAIN]")
    }
}