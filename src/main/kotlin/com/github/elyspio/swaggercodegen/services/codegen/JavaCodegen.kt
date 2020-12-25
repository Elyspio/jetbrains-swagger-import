package com.github.elyspio.swaggercodegen.services.codegen

import com.github.elyspio.swaggercodegen.core.GradleDependency
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.DependencyDialog
import com.github.elyspio.swaggercodegen.ui.DependencyDialog.Dependency
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog.SwaggerInfo
import com.github.elyspio.swaggercodegen.ui.format.JavaFormatInput
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

class JavaCodegen(private val info: SwaggerInfo) : ICodegen {


    private val dependencyList = listOf("swagger-annotations")


    override fun use(): List<String> = listOf(
        "--library",
        "retrofit2",
        "--additional-properties",
        "java8=true,hideGenerationTimestamp=true)"
    )

    override fun post() {
        extractFiles()
        changePackages()
        hightlightGradle()
        fixImports()
    }

    private fun fixImports() {
        val files = FileHelper.listJavaFiles(this.info.output)

        files.forEach {
            val lines = it.readLines().filter{ s -> !s.contains("CollectionFormats") }
            it.writeText(lines.joinToString(System.lineSeparator(), prefix = "", postfix = ""), Charset.forName("UTF-8"))
        }
    }

    private fun extractFiles() {
        val tempFolder = Path.of(System.getProperty("java.io.tmpdir"), "swagger-import")

        if (Files.exists(tempFolder)) {
            FileHelper.delete(tempFolder.toFile())
        }

        FileHelper.move(
            Path.of(info.output, "src", "main", "java", "io", "swagger", "client").toFile(),
            tempFolder.toFile()
        )

        FileHelper.delete(File(info.output))

        FileHelper.move(tempFolder.toFile(), File(info.output))

        val files = Files.list(Path.of(info.output)).toList();


        val keep = listOf("api", "model")

        files.forEach {
            val path = it.toAbsolutePath().toString()
            val lastPath = path.substring(path.lastIndexOf(File.separator), path.length)

            if(!keep.contains(lastPath.substring(1))) {
                it.toFile().deleteRecursively();
            }

        }


    }

    private fun hightlightGradle() {
        val gradle = GradleDependency(info.additionalParams[(JavaFormatInput.gradleBuildLocation)] as String)

        val dependencies = ArrayList<Dependency>()
        this.dependencyList.forEach {
            dependencies.add(Dependency(it, gradle.contains(it)))
        }

        DependencyDialog(dependencies).show()
    }

    private fun changePackages() {
        val files = FileHelper.listJavaFiles(info.output)
        if (!files.isEmpty()) {
            changePackageName(files)
        }
    }

    private fun changePackageName(files: Collection<File>) {

        val basePackage = info.additionalParams["package"] as String

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