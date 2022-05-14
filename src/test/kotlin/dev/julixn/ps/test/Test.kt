package dev.julixn.ps.test

fun main() {
    println(parseKey("abc"))
}

private fun parseKey(input: String): String {
    var output = ""
    input.chars().forEach { char -> output += char.toInt() }
    return output
}