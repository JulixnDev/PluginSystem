package dev.julixn.ps.systems.manager

import dev.julixn.ps.api.callbacks.LoadingCallback
import dev.julixn.ps.api.callbacks.PluginCallback
import dev.julixn.ps.systems.Plugin
import dev.julixn.ps.api.events.Event
import dev.julixn.ps.api.eventsystem.EventSubscriber
import dev.julixn.ps.api.license.LicenseController
import dev.julixn.ps.api.license.UseLicense
import dev.julixn.ps.api.logging.Logger
import java.lang.reflect.Method
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinFunction

class PluginManager(private val logger: Logger) {

    companion object {
        val plugins: HashMap<String, Plugin> = hashMapOf()
        val pluginCallbacks: HashMap<Plugin, PluginCallback> = hashMapOf()
        val eventClasses: HashMap<KClass<out Event>, ArrayList<KClass<*>>> = hashMapOf()
        val eventFunctionEvent: HashMap<Method?, KClass<out Event>> = hashMapOf()
        val licensePlugin: HashMap<Plugin, String> = hashMapOf()
    }

    private fun registerPlugin(plugin: Plugin) {
        plugins[plugin.getName()] = plugin
    }

    private fun unregisterPlugin(plugin: Plugin) {
        if(!plugin.isValid())
            return

        plugins.remove(plugin.getName())
    }

    private fun unregisterPlugin(pluginName: String) {
        plugins.remove(pluginName)
    }

    fun loadPlugin(plugin: Plugin) : Boolean {
        if(!plugin.isValid())
            return false

        if(plugin.clazz.annotations.find { it is UseLicense } != null)
            licensePlugin[plugin] = (plugin.clazz.annotations.find { it is UseLicense } as UseLicense).token

        val callback = LoadingCallback()
        plugin.load(callback)

        if(!callback.isSuccess())
            return false

        registerPlugin(plugin)
        return true
    }

    fun enablePlugins(debug: Boolean = false) {
        plugins.forEach { (_, plugin) ->
            val pluginCallback: PluginCallback = if(licensePlugin.contains(plugin))
                PluginCallback(licensePlugin[plugin]?.let { LicenseController(it, logger) })
            else
                PluginCallback(null)

            plugin.enable(pluginCallback)

            if(!pluginCallback.hasLicense) {
                logger.log("Can't enable plugin: ${plugin.getName()}[${plugin.getVersion()}] : No valid license!", "[PM]")
                return
            }


            pluginCallbacks[plugin] = pluginCallback

            logger.log("Enabled plugin: ${plugin.getName()}[${plugin.getVersion()}]", "[PM]")
            if(debug) logger.log("- Found ${pluginCallback.eventHandler.size} handler", "[PM]")

            pluginCallback.eventHandler.forEach(Consumer { clazz ->
                clazz.functions.forEach(Consumer { function ->
                    if ((function.annotations.find { annotation -> annotation is EventSubscriber }) != null && function.parameters.size >= 2) {
                        val paramPart: String = function.parameters[1].toString().split(":")[0]
                        val parameter = paramPart.substringAfter("(").replace(")", "") // .substring(0, paramPart.substringAfter("(").length - 1)
                        if(debug) logger.log("- Found Event Subscriber [ ${function.name} | $parameter ] in ${clazz.simpleName}", "[PM]")
                        try {
                            val classReference: KClass<out Event> = if (parameter.contains(","))
                                Class.forName(parameter.split(",")[0]).kotlin as KClass<out Event>
                            else
                                Class.forName(parameter).kotlin as KClass<out Event>

                            if(debug) logger.log("  - Event Class: ${classReference.simpleName}", "[PM]")
                            if(eventClasses[classReference] == null)
                                eventClasses[classReference] = arrayListOf(clazz)
                            else
                                eventClasses[classReference]?.add(clazz)

                            eventFunctionEvent[function.javaMethod] = classReference
                        } catch(exception: Exception) {
                            println(exception)
                        }
                    }
                })
            })
        }
    }

    fun sendEvent(event: Event) {
        eventClasses[event::class]?.forEach { function: KClass<*> ->
            function.java.methods.forEach {
                if(it.isAnnotationPresent(EventSubscriber::class.java) && eventFunctionEvent[it.kotlinFunction?.javaMethod]?.equals(event::class) == true)
                    it.invoke( function.createInstance(), event)
            }
        }

    }
}