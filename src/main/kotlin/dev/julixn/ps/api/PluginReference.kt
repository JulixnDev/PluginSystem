package dev.julixn.ps.api

import kotlin.reflect.KClass

class PluginReference(pluginClass: KClass<out IPlugin>) {

    private val clazz: KClass<out IPlugin> = pluginClass
    private val plugin: IPlugin = clazz.java.getDeclaredConstructor().newInstance() as IPlugin

    fun getName(): String {
        return (this.clazz.annotations.find { it is PluginMain } as PluginMain).name
    }

    fun getVersion(): String {
        return (this.clazz.annotations.find { it is PluginMain } as PluginMain).version
    }
}

