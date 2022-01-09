package com.github.elyspio.swaggercodegen.services

import com.github.elyspio.swaggercodegen.helper.ConfigHelper
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.helper.swagger.maven.MavenParser
import com.github.elyspio.swaggercodegen.helper.swagger.maven.Version
import com.github.elyspio.swaggercodegen.services.generators.codegen.ICodegen
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.MessageType
import com.intellij.util.io.exists
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class SwaggerService {

    init {
        FileHelper.pluginFolder.toFile().mkdirs()


        val latestVersion = MavenParser.getLatest()

        if (!this.isLibDownloaded() || ConfigHelper.forceDownload || ConfigHelper.engineVersion < latestVersion) {
            println("Swagger Import - Lib is not present or a new version is available, downloading it")
            downloadLib(latestVersion)
        }
    }


    private fun isLibDownloaded(): Boolean {
        return FileHelper.getJarPath().exists()
    }


    private fun downloadLib(version: Version) {

        val website = URL(
            "https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$version/openapi-generator-cli-$version.jar"
        )

        website.openStream().use { inputStream ->
            Files.copy(
                inputStream, FileHelper.getJarPath(), StandardCopyOption.REPLACE_EXISTING
            )
        }

        ConfigHelper.engineVersion = version
        ConfigHelper.forceDownload = false
    }


    fun generate(info: SwaggerFormData): Boolean {

        try {
            URL(info.url).readText()
        } catch (e: Exception) {
            NotificationService.createNotification("Could not reach ${info.url}", MessageType.ERROR)
            return false
        }

        val codegen = ICodegen.of(info)

        try {

            when {
                info.format.codegen != null -> {
                    val command = mutableListOf(
                        "java", "-jar", FileHelper.getJarPath().toString(), "generate", "--skip-validate-spec", "-i", info.url, "-g", info.format.codegen!!, "-o", info.output
                    )

                    codegen.let { command.addAll(it.use()) }

                    val processBuilder = ProcessBuilder(command)
                        .inheritIO()
                        .directory(FileHelper.getBundleJavaFolder().toFile())

                    val process = processBuilder.start()

                    process.waitFor()
                    codegen.post()
                    return process.exitValue() == 0
                }
                else -> {
                    codegen.post()
                }
            }
        } catch (e: Exception) {
            val logger = Logger.getInstance("")
            logger.error(e)
            Notifications.Bus.notify(Notification("ADB Logs", "Swagger error", e.stackTrace.toString(), NotificationType.ERROR))
            return false
        } finally {
            ConfigHelper.history = info
        }





        return true
    }


}