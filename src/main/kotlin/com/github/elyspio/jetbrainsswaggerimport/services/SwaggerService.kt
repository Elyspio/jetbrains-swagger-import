package com.github.elyspio.jetbrainsswaggerimport.services

import com.github.elyspio.jetbrainsswaggerimport.helper.FileHelper
import com.intellij.util.io.exists
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
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

    fun import(dest: String, format: String, url: String) {
        val processBuilder = ProcessBuilder(
            "java", "-jar", FileHelper.getJarPath().toString(),
            "generate", "-i", url, "-l", format, "-o", dest
        )
        processBuilder.directory(FileHelper.getBundleJavaFolder().toFile())
        val process = processBuilder.start()
        process.waitFor()
        var result = StringBuilder(80)
        BufferedReader(InputStreamReader(process.inputStream)).use { `in` ->
            while (true) {
                val line = `in`.readLine() ?: break
                result.append(line).append("\r\n")
            }
        }
        print("log $result")
        result = StringBuilder()
        BufferedReader(InputStreamReader(process.errorStream)).use { `in` ->
            while (true) {
                val line = `in`.readLine() ?: break
                result.append(line).append("\r\n")
            }
        }
        print("err $result")

    }

}