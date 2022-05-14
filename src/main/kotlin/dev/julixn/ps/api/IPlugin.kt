package dev.julixn.ps.api

import dev.julixn.ps.api.callbacks.LoadingCallback
import dev.julixn.ps.api.callbacks.PluginCallback
import dev.julixn.ps.api.logging.Logger

interface IPlugin {

    fun load(callback: LoadingCallback)

    fun enable(callback: PluginCallback)

    fun disable()

    fun getLogger(prefix: String, logTime: Boolean = false): Logger {
        return Logger(prefix, logTime)
    }

}