package dev.julixn.ps.api.callbacks

import dev.julixn.ps.api.eventsystem.EventHandler
import dev.julixn.ps.api.license.LicenseController
import kotlin.reflect.KClass

class PluginCallback(private val licenseController: LicenseController?) {

    val eventHandler: ArrayList<KClass<out EventHandler>> = arrayListOf()

    fun registerEventHandler(handler: KClass<out EventHandler>) {
        if(!eventHandler.contains(handler))
            this.eventHandler.add(handler)
    }

    fun unregisterEventHandler(handler: KClass<out EventHandler>) {
        if(eventHandler.contains(handler))
            this.eventHandler.remove(handler)
    }

    fun getLicenseController(): LicenseController? {
        return licenseController
    }

}