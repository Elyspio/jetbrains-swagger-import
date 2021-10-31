package com.github.elyspio.swaggercodegen.services.generators.codegen

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

class TypescriptCodegen(private val info: SwaggerFormData) : ICodegen {

    override fun use(): List<String> = listOf("--additional-properties", "usePromise=true,supportsES6=true")

    override fun post() {
        removeNonTypescriptFiles()
        ensureFileContent()
    }

    private fun ensureFileContent() {
        val files = FileHelper.listTypescriptFiles(this.info.output)

        files.forEach {
            var text = it.readText()

            if (text.isEmpty()) {
                text = "export {}"
            }

            it.writeText(text, Charset.forName("UTF-8"))
        }
    }

    private fun removeNonTypescriptFiles() {

        val typescriptFiles = FileHelper.listTypescriptFiles(this.info.output)
        val allFiles = FileHelper.listFile(this.info.output)

        val filesToRemove = allFiles.filter { f -> !typescriptFiles.contains(f) }

        filesToRemove.forEach {
            if (it.isFile) it.delete()
        }

        Files.list(Path.of(this.info.output)).forEach {
            if (it.toFile().isDirectory) {
                val innerFiles = FileHelper.listTypescriptFiles(it.toFile().absolutePath)
                if (innerFiles.isEmpty()) {
                    it.toFile().deleteRecursively()
                }
            }
        }


    }
}