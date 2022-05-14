package dev.julixn.ps.api.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.julixn.ps.api.IPlugin
import dev.julixn.ps.api.PluginReference
import dev.julixn.ps.systems.Plugin
import java.io.File
import java.io.Reader
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Paths


class PluginConfigManager(pluginClass: Class<out IPlugin>) {

    private val gson: Gson = GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create()

    private val pluginInstance: Plugin = Plugin(pluginClass.kotlin)
    private lateinit var folder: File

    private fun createPluginDirectory() {
        val name = this.pluginInstance.getName()
        this.folder = File("plugins/$name")

        if(!this.folder.exists())
            this.folder.mkdirs()
    }

    fun saveConfigObject(name: String, configObject: PluginConfigObject) {
        createPluginDirectory()
        val configFile = File(this.folder, "$name.json")
        if(!configFile.exists())
            configFile.createNewFile()

        try {
            val writer: Writer = Files.newBufferedWriter(Paths.get(configFile.absolutePath))
            gson.toJson(configObject, writer)
            writer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun saveConfigObjectCollection(name: String, configObjects: PluginConfigObjectCollection<*>) {
        createPluginDirectory()
        val configFile = File(this.folder, "$name.json")
        if(!configFile.exists())
            configFile.createNewFile()

        try {
            val writer: Writer = Files.newBufferedWriter(Paths.get(configFile.absolutePath))
            gson.toJson(configObjects, writer)
            writer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loadConfigObject(name: String, configObjectClassReference: Class<out PluginConfigObject>): PluginConfigObject? {
        createPluginDirectory()
        val files: Array<File> = this.folder.listFiles { _, fileName -> fileName.endsWith(".json") && fileName.equals("$name.json") } as Array<File>
        if(((this.folder.listFiles()?.size ?: 0) == 0) || files.isEmpty())
            return null

        try {
            val reader: Reader = Files.newBufferedReader(Paths.get(files[0].absolutePath))
            val configObject: PluginConfigObject = gson.fromJson(reader, configObjectClassReference)
            reader.close()
            return configObject
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            return null
        }
    }

    fun loadConfigObjectCollection(name: String, configObjectClassReference: Class<out PluginConfigObjectCollection<*>>): PluginConfigObjectCollection<*>? {
        createPluginDirectory()
        val files: Array<File> = this.folder.listFiles { _, fileName -> fileName.endsWith(".json") && fileName.equals("$name.json") } as Array<File>
        if(((this.folder.listFiles()?.size ?: 0) == 0) || files.isEmpty())
            return null

        try {
            val reader: Reader = Files.newBufferedReader(Paths.get(files[0].absolutePath))
            val configObjectCollection: PluginConfigObjectCollection<*> = gson.fromJson(reader, configObjectClassReference)
            reader.close()
            return configObjectCollection
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            return null
        }
    }

}