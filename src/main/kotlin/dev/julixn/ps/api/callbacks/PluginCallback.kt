package dev.julixn.ps.api.callbacks

import dev.julixn.ps.api.eventsystem.EventHandler
import kotlin.reflect.KClass

class PluginCallback {

    val eventHandler: ArrayList<KClass<out EventHandler>> = arrayListOf()

    fun registerEventHandler(handler: KClass<out EventHandler>) {
        if(!eventHandler.contains(handler))
            this.eventHandler.add(handler)
    }

    fun unregisterEventHandler(handler: KClass<out EventHandler>) {
        if(eventHandler.contains(handler))
            this.eventHandler.remove(handler)
    }

}