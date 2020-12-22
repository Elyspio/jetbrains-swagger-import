package com.github.elyspio.swaggercodegen.services.codegen

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog.SwaggerInfo
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

class JavaCodegen(private val info: SwaggerInfo) : ICodegen {


    override fun use(): List<String> = listOf(
        "--library",
        "retrofit2",
        "--additional-properties",
        "java8=true,useRxJava2=true,hideGenerationTimestamp=true)"
    )

    override fun post() {


        val tempFolder = Path.of(System.getProperty("java.io.tmpdir"), "swagger-import")

        if (Files.exists(tempFolder)) {
            FileUtils.deleteDirectory(tempFolder.toFile())
        }

        FileUtils.moveDirectory(
            Path.of(info.output, "src", "main", "java", "io", "swagger", "client").toFile(),
            tempFolder.toFile()
        )

        FileUtils.deleteDirectory(File(info.output))

        FileUtils.moveDirectory(tempFolder.toFile(), File(info.output))


        val files = FileHelper.listJavaFiles(info.output)
        println(files)
        if (files != null) {
            changePackageName(files)
        }

    }




    private fun changePackageName(files: Collection<File>) {

        val basePackage = info.additionalParams["package"] as String // & entre les deux arrays


        files.forEach {
            val packageName = FileHelper.getPackage(it.path)
            println(packageName)
            val content = it.readLines(Charset.forName("UTF-8")).toMutableList()
//            content[0] = "package $packageName;"

            for ((index, line) in content.withIndex()) {
                content[index] = line.replace("io.swagger.client", basePackage)
            }


            it.writeText(content.joinToString(separator = System.lineSeparator(), prefix = "", postfix = ""), Charset.forName("UTF-8"))
        }
    }

}