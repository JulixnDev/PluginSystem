package dev.julixn.ps.api.license

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.net.HttpURLConnection
import java.net.URL

class LicenseController(private val token: String) {

    fun checkLicense(license: License): Boolean {
        val url = URL("http://localhost:3000/license/${parseToken(token)}/check/${license.key}")

        var response: JsonElement? = null
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response = JsonParser.parseString(line)
                }
            }
        }

        return response?.asJsonObject?.get("valid")?.asBoolean ?: false
    }

    fun parseToken(token: String): String {
        var output: String? = null
        token.chars().forEach { char -> output += char }
        if(output == null)
            return token

        return output.replace("null", "")
    }

}