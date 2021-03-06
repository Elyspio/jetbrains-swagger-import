package com.github.elyspio.swaggercodegen.services

import com.github.elyspio.swaggercodegen.config.Conf
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.services.generators.codegen.ICodegen
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import com.intellij.openapi.ui.MessageType
import com.intellij.util.io.exists
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class SwaggerService {

    init {
        FileHelper.pluginFolder.toFile().mkdirs()
        if (!this.isLibDownloaded() || Conf.forceDownload) {
            println("Swagger Import - Lib is not present, downloading it")
            downloadLib()
        }
    }


    private fun isLibDownloaded(): Boolean {
        return FileHelper.getJarPath().exists()
    }


    private fun downloadLib() {

        val website = URL(
            "https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/5.0.1/openapi-generator-cli-5.0.1.jar"
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


    fun generate(info: SwaggerFormData): Boolean {

        // Ensure that enpoint is valid
        try {
            URL(info.url).readText()
        } catch (e: Exception) {
            NotificationService.createNotification("Could not reach ${info.url}", MessageType.ERROR)
            return false
        }

        val codegen = ICodegen.of(info)


        when {
            info.format.codegen != null -> {
                val command = ArrayList(
                    listOf(
                        "java", "-jar", FileHelper.getJarPath().toString(),
                        "generate", "-i", info.url, "-g", info.format.codegen, "-o", info.output
                    )
                )

                codegen?.let { it -> command.addAll(it.use()) }

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
                codegen?.post()
                return process.exitValue() == 0
            }

            else -> {
                codegen?.post()
            }
        }




       return true;
    }


}