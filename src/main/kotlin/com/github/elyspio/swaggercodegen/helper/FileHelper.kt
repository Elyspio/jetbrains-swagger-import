package com.github.elyspio.swaggercodegen.helper

import com.intellij.util.io.isFile
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import org.apache.commons.io.filefilter.WildcardFileFilter
import java.io.File
import java.nio.file.Files
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

        private fun getJavaPath(): Path {
            return getBundleJavaFolder().resolve("java")
        }


        fun listJavaFiles(path: Path): MutableCollection<File>? {
            return FileUtils.listFiles(path.toFile(), WildcardFileFilter("*.java"), TrueFileFilter.TRUE)
        }

        fun listJavaFiles(path: String): MutableCollection<File>? {
            return listJavaFiles(Path.of(path))
        }


        fun getPackage(file: String): String {
            return this.getPackage(Path.of(file))
        }

        fun getPackage(file: Path): String {
            val paths = file.toFile().path.split(File.separator)

            val minus = if (file.isFile()) 1 else 0

            return paths.subList(paths.lastIndexOf("java") + 1, paths.size - minus).joinToString(separator = ".", prefix = "", postfix = "")
        }


        fun getGradleBuild(folder: String): String? {
            return getGradleBuild(Path.of(folder))
        }


        fun getGradleBuild(folder: Path): String? {
            val matchs = listOf("build.gradle", "build.gradle.kts")

            fun contains(dir: Path): Boolean {
                val files = Files.list(dir)

                for (file in files) {
                    for (match in matchs) {
                        if (file.toFile().path.contains(match)) {
                            return true
                        }
                    }
                }
                return false
            }


            var run = true
            var current = folder
            while (run) {
                if (contains(current)) {
                    val files = Files.list(current)
                    return files.filter { path -> matchs.any { m -> path.toString().contains(m) } }.findFirst().get().toString()
                }
                current = current.parent

                if (current.toFile().path.length <= 3) {
                    run = false
                }
            }

            return null


        }


    }
}