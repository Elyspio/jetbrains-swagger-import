package com.github.elyspio.swaggercodegen.services.generators.test.typescript

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.helper.swagger.SwaggerParser
import com.github.elyspio.swaggercodegen.helper.swagger.SwaggerParser.ALL_CONTROLLERS
import com.github.elyspio.swaggercodegen.services.SwaggerService
import com.github.elyspio.swaggercodegen.services.generators.codegen.ICodegen
import com.github.elyspio.swaggercodegen.ui.AdditionalParams
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import com.jetbrains.rd.framework.base.deepClonePolymorphic
import java.io.File


class TypescriptTestUnit(private val info: SwaggerFormData) : ICodegen {

    override fun use(): List<String> = listOf()

    override fun post() {
        val swaggerDefinition = SwaggerParser.extract(info)
        val projectFiles = findServerPath(info.output)

        val testFolder = File(projectFiles.srcFile, "tests")
        val restFolder = File(testFolder, "rest")
        val controllerFolder = File(restFolder, "controllers")

        controllerFolder.mkdirs()

        val selectedController = info.additionalParams.typeScriptTestUnit.controllers

        if (selectedController.contains(ALL_CONTROLLERS)) {
            info.additionalParams.typeScriptTestUnit.controllers = swaggerDefinition.tags.map { it.name }
        } else {
            info.additionalParams.typeScriptTestUnit.controllers = listOf(ALL_CONTROLLERS)
        }

        selectedController.toList().forEach {

            val newInfo = info.deepClonePolymorphic()
            newInfo.additionalParams.typeScriptTestUnit.controllers = listOf(it)
            val newSwaggerDefinition = SwaggerParser.extract(newInfo)

            var path = projectFiles.serverFile.relativeTo(File(controllerFolder.path)).path
            path = path.substringBeforeLast(".")
            val template = TypescriptTemplate.fill(newSwaggerDefinition)

            val str = """
                import {Server} from "${path.replace("\\", "/")}"
                import {PlatformExpress} from "@tsed/platform-express";
                import * as Apis from "../api";
                """.trimIndent() + "\n" + template
            File(controllerFolder, "${normalizeControllerName(it)}.test.ts").writeText(str)
        }


        // Generate Client Api
        val apiFolder = File(restFolder, "api")
        apiFolder.mkdirs()
        SwaggerService().generate(
            SwaggerFormData(
                output = apiFolder.toString(),
                format = Format.TypeScriptAxios,
                url = info.url,
                additionalParams = AdditionalParams()
            )
        )

    }


    private fun findServerPath(dir: String): TsProjectInfo {

        var found = false
        var srcFolder = File(dir)
        var folders = srcFolder.list()?.toList()
        while (!found) {
            if (folders?.contains("src") == true) {
                srcFolder = File(srcFolder, "src")
                found = true
            } else if (srcFolder.path.substringAfterLast("/") == "src") {
                found = true

            }
            srcFolder = srcFolder.parentFile
            folders = srcFolder.list()?.toList()
        }

        val tsFiles = FileHelper.listTypescriptFiles(srcFolder.path, true)

        var content: String
        tsFiles.forEach {
            content = it.readText()
            if (content.contains("export class Server")) return TsProjectInfo(srcFolder, it)
        }

        throw Exception("Could not find 'src' folder in '$dir' or its parent")
    }

    private fun normalizeControllerName(original: String): String {
        var new = original
            .replace(Regex("([A-Z])"), ".$1")
            .replace(" ", "")
            .toLowerCase()

        if (new[0] == '.') new = new.substring(1)
        return new
    }


}

data class TsProjectInfo(
    val srcFile: File,
    val serverFile: File
)