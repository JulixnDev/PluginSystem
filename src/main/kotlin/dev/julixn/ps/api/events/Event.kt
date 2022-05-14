package dev.julixn.ps.api.events

abstract class Event(private val name: String) {

    fun getName(): String {
        return this.name
    }

}