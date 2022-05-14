package dev.julixn.ps.systems.loader

import dev.julixn.ps.api.IPlugin
import dev.julixn.ps.api.logging.Logger
import dev.julixn.ps.systems.Plugin
import dev.julixn.ps.systems.files.FileManager
import dev.julixn.ps.systems.manager.PluginManager
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.function.Consumer
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.reflect.KClass


class PluginLoader(private val logger: Logger, private val pluginManager: PluginManager) {

    companion object {
        const val PLUGIN_FOLDER = "plugins"
    }

    fun load() {
        if (!FileManager.createFolder(PLUGIN_FOLDER))
            return;

        val urls = ArrayList<URL>()
        val classes = ArrayList<String>()
        FileManager.getJarFiles(PLUGIN_FOLDER)?.forEach { file ->
            try {
                val jarFile = JarFile(file)
                urls.add(URL("jar:file:$PLUGIN_FOLDER" + "/" + file.name + "!/"))
                jarFile.stream().forEach { jarEntry: JarEntry ->
                    if (jarEntry.name.endsWith(".class")) {
                        classes.add(jarEntry.name)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val pluginLoader = URLClassLoader(urls.toTypedArray())
        classes.forEach(Consumer { s: String ->
            try {
                val clazz = pluginLoader.loadClass(s.replace("/".toRegex(), ".").replace(".class", ""))
                for (anInterface in clazz.interfaces) {
                    if (anInterface == IPlugin::class.java) {
                        val plugin = Plugin(clazz.kotlin as KClass<out IPlugin>)
                        logger.log("Loading ${plugin.getName()}[${plugin.getVersion()}]", "[PL]")
                        pluginManager.loadPlugin(plugin)
                        break
                    }
                }
            } catch (exception: ClassNotFoundException) {
                exception.printStackTrace()
            } catch (exception: InstantiationException) {
                exception.printStackTrace()
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        })
    }
}