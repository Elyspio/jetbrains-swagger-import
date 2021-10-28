package com.github.elyspio.swaggercodegen.helper

import com.intellij.util.io.isFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


object FileHelper {

    val pluginFolder: Path = Path.of(getAppdataFolder(), "jetbrains-plugins", "swagger-import")

    private fun getAppdataFolder(): String {
        val os = (System.getProperty("os.name")).toUpperCase()

        return if (os.contains("WIN")) System.getenv("LocalAppdata")
        else System.getProperty("user.home")

    }

    fun getJarPath(): Path {
        return pluginFolder.resolve("import.jar")
    }


    fun getConfigPath(): Path {
        return pluginFolder.resolve("config.json")
    }


    fun getBundleJavaFolder(): Path {
        return Path.of(System.getProperty("java.home"), "bin")
    }

    private fun getJavaPath(): Path {
        return getBundleJavaFolder().resolve("java")
    }


    fun listJavaFiles(path: Path): Collection<File> {
        return listFile(path, "java")
    }

    fun listJavaFiles(path: String): Collection<File> {
        return listJavaFiles(Path.of(path))
    }

    fun listTypescriptFiles(path: Path, ignoreNodeModules: Boolean = false): Collection<File> {
        return listFile(path, "ts").filter { !it.path.contains("node_modules") || !ignoreNodeModules }
    }

    fun listTypescriptFiles(path: String, ignoreNodeModules: Boolean = false): Collection<File> {
        return listTypescriptFiles(Path.of(path), ignoreNodeModules)
    }


    fun listFile(path: Path, extension: String? = null): MutableCollection<File> {

        val files = mutableListOf<File>()
        path.toFile().walkTopDown().forEach {
            when (extension) {
                null -> {
                    files.add(it)
                }
                else -> {
                    if (it.path.substring(it.path.length - (extension.length + 1), it.path.length).contains(".$extension")) files.add(it)
                }
            }
        }
        return files
    }

    fun listFile(path: String, extension: String? = null): MutableCollection<File> {
        return listFile(Path.of(path), extension)
    }


    fun getPackage(file: String): String {
        return this.getPackage(Path.of(file))
    }

    fun getPackage(file: Path): String {
        val paths = file.toFile().path.split(File.separator)

        val minus = if (file.isFile()) 1 else 0

        var index = paths.indexOf("java")
        if (index == -1) index = paths.indexOf("kotlin")

        return paths.subList(index + 1, paths.size - minus).joinToString(separator = ".", prefix = "", postfix = "")
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


    fun delete(file: File) {
        file.deleteRecursively()
    }

    fun move(src: File, dest: File) {
        src.copyRecursively(dest, true)
        delete(src)
    }

    /**
     * @return the last part of the path (with extension)
     */
    fun getFilename(node: File): String {
        return node.path.substringAfterLast("/")
    }


}