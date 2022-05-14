package dev.julixn.ps.systems

import dev.julixn.ps.api.IPlugin
import dev.julixn.ps.api.callbacks.LoadingCallback
import dev.julixn.ps.api.PluginMain
import dev.julixn.ps.api.callbacks.PluginCallback
import kotlin.reflect.KClass

class Plugin(pluginClass: KClass<out IPlugin>) : IPlugin {

    val clazz: KClass<out IPlugin> = pluginClass
    private val plugin: IPlugin = clazz.java.getDeclaredConstructor().newInstance() as IPlugin

    override fun enable(callback: PluginCallback) {
        this.plugin.enable(callback)
    }

    override fun disable() {
        this.plugin.disable()
    }

    override fun load(callback: LoadingCallback) {
        this.plugin.load(callback)
    }

    fun isValid(): Boolean {
        return this.clazz.annotations.find { it is PluginMain } != null
    }

    fun getName(): String {
        return (this.clazz.annotations.find { it is PluginMain } as PluginMain).name
    }

    fun getVersion(): String {
        return (this.clazz.annotations.find { it is PluginMain } as PluginMain).version
    }
}