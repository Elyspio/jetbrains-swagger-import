package com.github.elyspio.swaggercodegen.services.generators.codegen.jvm

import com.github.elyspio.swaggercodegen.core.GradleDependency
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.services.generators.codegen.ICodegen
import com.github.elyspio.swaggercodegen.ui.DependencyDialog.Dependency
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

abstract class JvmCodegen(private val info: SwaggerFormData) : ICodegen {


    /**
     * Dependencies that the presence will be checked in build.gradle file
     */
    abstract val dependencyList: List<String>

    /**
     * Generated folder to keep
     */
    abstract val keepFolders: List<String>

    /**
     * Additional properties pass to the lib
     */
    abstract val additionalProperties: List<String>

    /**
     * Path where sources' main folders are generated
     */
    abstract val baseFolderPath: List<String>

    override fun use(): List<String> {
        return additionalProperties
    }

    override fun post() {
        extractFiles()
        changePackages()
        hightlightGradle()
        fixImports()
    }

    private fun fixImports() {
        val files = FileHelper.listJvmFiles(this.info.output)

        files.forEach {
            val lines = it.readLines().filter { s -> !s.contains("CollectionFormats") }
            it.writeText(lines.joinToString(System.lineSeparator(), prefix = "", postfix = ""), Charset.forName("UTF-8"))
        }
    }

    protected fun extractFiles() {
        val tempFolder = Path.of(System.getProperty("java.io.tmpdir"), "swagger-import")

        if (Files.exists(tempFolder)) {
            FileHelper.delete(tempFolder.toFile())
        }

        FileHelper.move(
            Path.of(info.output, *baseFolderPath.toTypedArray()).toFile(),
            tempFolder.toFile()
        )

        FileHelper.delete(File(info.output))

        FileHelper.move(tempFolder.toFile(), File(info.output))

        val files = Files.list(Path.of(info.output))

        files.forEach {
            val path = it.toAbsolutePath().toString()
            val lastPath = path.substring(path.lastIndexOf(File.separator), path.length)

            if (!keepFolders.contains(lastPath.substring(1))) {
                it.toFile().deleteRecursively()
            }

        }
    }

    protected fun hightlightGradle() {
        val gradle = GradleDependency(info.additionalParams.jvm.gradleBuildLocation)

        val dependencies = ArrayList<Dependency>()
        this.dependencyList.forEach {
            dependencies.add(Dependency(it, gradle.contains(it)))
        }

//        ApplicationManager.getApplication().runReadAction {
//            DependencyDialog(dependencies).show()
//        }


    }

    protected fun changePackages() {
        val files = FileHelper.listJvmFiles(info.output)
        if (!files.isEmpty()) {
            changePackageName(files)
        }
    }

    private fun changePackageName(files: Collection<File>) {

        val basePackage = info.additionalParams.jvm.packagePath

        files.forEach {
            val packageName = FileHelper.getJvmPackage(it.path)
            val content = it.readLines(Charset.forName("UTF-8")).toMutableList()
//            content[0] = "package $packageName;"

            for ((index, line) in content.withIndex()) {
                content[index] = line.replace("org.openapitools.client", basePackage)
            }


            it.writeText(content.joinToString(separator = System.lineSeparator(), prefix = "", postfix = ""), Charset.forName("UTF-8"))
        }
    }

}