package com.github.elyspio.swaggercodegen.services

import org.apache.commons.io.FileUtils;


import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.intellij.util.io.exists
import com.jetbrains.rd.util.string.print
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


class SwaggerService {


    init {
        FileHelper.pluginFolder.toFile().mkdirs()
        if (!this.isLibDownloaded()) {
            println("Swagger Import - Lib is not present, downloading it")
            downloadLib()
        }
    }


    private fun isLibDownloaded(): Boolean {
        return FileHelper.getJarPath().exists()
    }


    private fun downloadLib() {

        val website = URL(
            "https://repo1.maven.org/maven2/io/swagger/codegen/v3/swagger-codegen-cli/3.0.23/swagger-codegen-cli-3.0.23.jar"
        )

        website
            .openStream()
            .use { inputStream ->
                Files.copy(
                    inputStream,
                    FileHelper.getJarPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
    }

    private fun postImport(dest: String, format: Format) {
        if (format == Format.JavaRetrofit2) {
//            val folderName = dest.substring(dest.lastIndexOf("/") + 1)

            val tempFolder = Path.of(System.getProperty("java.io.tmpdir"), "swagger-import")

            FileUtils.copyDirectory(
                Path.of(dest, "src", "main", "java", "io", "swagger", "client").toFile(),
                tempFolder.toFile()
            )

            FileUtils.deleteDirectory(File(dest))

            FileUtils.copyDirectory(tempFolder.toFile(), File(dest))


        }
    }

    fun import(dest: String, format: Format, url: String): Boolean {

        val command = ArrayList(listOf(
            "java", "-jar", FileHelper.getJarPath().toString(),
            "generate", "-i", url, "-l", format.codegen, "-o", dest
        ))

        if (format == Format.JavaRetrofit2) {
            command.addAll(listOf(
                "--library",
                "retrofit2",
                "--additional-properties",
                "java8=true,useRxJava2=true")
            )

        }


        val processBuilder = ProcessBuilder(command)
        processBuilder.directory(FileHelper.getBundleJavaFolder().toFile())
        val process = processBuilder.start()
        BufferedReader(InputStreamReader(process.inputStream)).use { `in` ->
            while (true) {
                val line = `in`.readLine() ?: break
                println(line)
            }
        }
        BufferedReader(InputStreamReader(process.errorStream)).use { `in` ->
            while (true) {
                val line = `in`.readLine() ?: break
                println(line)
            }
        }
        process.waitFor()


        postImport(dest, format)

        return process.exitValue() != 0
    }


}