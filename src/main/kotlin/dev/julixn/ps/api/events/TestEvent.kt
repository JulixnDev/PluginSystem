package dev.julixn.ps.api.events

class TestEvent(id: Number) : Event("Test") {

    private val localId = id;

    fun test() {
        println("EVENT[$localId] : test!")
    }

}