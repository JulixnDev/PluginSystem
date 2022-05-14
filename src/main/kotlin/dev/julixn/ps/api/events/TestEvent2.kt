package dev.julixn.ps.api.events

class TestEvent2(test: String) : Event("test_2") {

    val str = test

    fun cool(input: String) {
        println("[${getName()} | $str] $input")
    }

}