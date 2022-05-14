package dev.julixn.ps.systems.files

import java.io.File

class FileManager {

    companion object {
        fun createFolder(name: String) : Boolean {
            val file: File = File(name)
            if(file.exists())
                return true
            return file.mkdirs()
        }

        fun getJarFiles(folder: String): Array<out File>? {
            return getJarFiles(File(folder))
        }

        private fun getJarFiles(folder: File): Array<out File>? {
            return folder.listFiles { _, name -> name.endsWith(".jar") }
        }
    }

}