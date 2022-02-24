package com.github.elyspio.swaggercodegen.services.generators.codegen.jvm

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData

class KotlinCodegen(info: SwaggerFormData) : JvmCodegen(info) {

    override val dependencyList = listOf("swagger-annotations")
    override val keepFolders = listOf("apis", "models")
    override val additionalProperties: List<String>
    override val baseFolderPath = listOf("src", "main", "kotlin", "org", "openapitools", "client")

    init {
        val params = mutableListOf(
                "--library", "jvm-retrofit2",
                "--additional-properties",
        )
        var additionalProps = "dateLibrary=java8,serializationLibrary=gson"
        if (info.format === Format.KotlinCoroutine) {
            additionalProps += ",useCoroutines=true"
        }
        params.add(additionalProps)
        this.additionalProperties = params.toList()
    }
}