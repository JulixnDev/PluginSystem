package dev.julixn.ps.api.logging

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger(prefix: String, logWithTime: Boolean = false) {

    private val logPrefix: String = prefix;
    private val logTime: Boolean = logWithTime;

    fun log(message: Any, subPrefix: String = "") {
        if(subPrefix != "")
            println("${preparePrefix()} $subPrefix $message")
        else
            println("${preparePrefix()} $message")
    }

    private fun preparePrefix(): String {
        var localPrefix = this.logPrefix
        val blankLetterArray: Array<Any> = removeLastBlank(localPrefix)
        if(blankLetterArray[0] == true) {
            localPrefix = blankLetterArray[1] as String
        }

        if(this.logTime)
            localPrefix = localPrefix.replace("%TIME%", getLogTime())

        return localPrefix
    }

    private fun removeLastBlank(input: String): Array<Any> {
        var changed = false
        var localInput: String = input
        val lastIndex = input.lastIndexOf(' ')
        if(lastIndex == input.length - 1){
            localInput = localInput.substring(0, input.length - 1)
            changed = true
        }

        if (input.elementAt(lastIndex - 1) == ' ')
            localInput = removeLastBlank(localInput)[1] as String

        return arrayOf(changed, localInput)
    }

    private fun getLogTime(): String {
        val dateTime = LocalDateTime.now()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

        return dateTime.format(dateTimeFormatter)
    }

}