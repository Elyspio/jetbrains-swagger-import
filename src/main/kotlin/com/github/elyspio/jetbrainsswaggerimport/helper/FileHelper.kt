package com.github.elyspio.jetbrainsswaggerimport.helper

import java.nio.file.Path


class FileHelper {
    companion object {

        val pluginFolder: Path = Path.of(getAppdataFolder(), "jetbrains-plugins", "swagger-import")

        private fun getAppdataFolder(): String {
            val os = (System.getProperty("os.name")).toUpperCase()

            return if (os.contains("WIN"))
                System.getenv("LocalAppdata")
            else
                System.getProperty("user.home")

        }

        fun getJarPath(): Path {
            return pluginFolder.resolve("import.jar")
        }


        fun getBundleJavaFolder(): Path {
            return Path.of(System.getProperty("java.home"), "bin")
        }

       private  fun getJavaPath(): Path {
            return getBundleJavaFolder().resolve("java")
        }

    }
}