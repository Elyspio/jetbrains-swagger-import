package com.github.elyspio.swaggercodegen.services.generators.codegen

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

class CSharpCodegen(private val info: SwaggerFormData) : ICodegen {

    override fun use(): List<String> = listOf(
            "--library", "httpclient",
            "--additional-properties", "targetFramework=net6.0,nullableReferenceTypes=true",
    )

    override fun post() {
        extractFiles()
        changePackages()
    }


    private fun changePackages() {
        val files = FileHelper.listCSharpFiles(info.output)
        if (!files.isEmpty()) {
            changePackageName(files)
        }
    }

    private fun changePackageName(files: Collection<File>) {

        val namespace = info.additionalParams.csharp.namespace

        files.forEach {
            val content = it.readLines(Charset.forName("UTF-8")).toMutableList()

            for ((index, line) in content.withIndex()) {
                content[index] = line.replace("Org.OpenAPITools", namespace)
            }


            it.writeText(content.joinToString(separator = System.lineSeparator(), prefix = "", postfix = ""), Charset.forName("UTF-8"))
        }
    }


    private fun extractFiles() {
        val tempFolder = Path.of(System.getProperty("java.io.tmpdir"), "swagger-import")

        if (Files.exists(tempFolder)) {
            FileHelper.delete(tempFolder.toFile())
        }

        FileHelper.move(
                Path.of(info.output, "src", "Org.OpenAPITools").toFile(),
                tempFolder.toFile()
        )

        FileHelper.delete(File(info.output))

        FileHelper.move(tempFolder.toFile(), File(info.output))

        FileHelper.delete(Path.of(info.output, "Org.OpenAPITools.csproj").toFile())
    }
}