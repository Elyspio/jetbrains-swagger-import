package com.github.elyspio.swaggercodegen.services

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.services.codegen.ICodegen
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
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


    fun import(info: SwaggerDialog.SwaggerInfo): Boolean {

        val command = ArrayList(
            listOf(
                "java", "-jar", FileHelper.getJarPath().toString(),
                "generate", "-i", info.url, "-l", info.format.codegen, "-o", info.output
            )
        )

        val codegen = ICodegen.of(info)

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


}