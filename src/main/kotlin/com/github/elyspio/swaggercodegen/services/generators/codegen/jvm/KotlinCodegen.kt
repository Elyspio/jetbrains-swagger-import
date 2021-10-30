package com.github.elyspio.swaggercodegen.services.generators.codegen.jvm

import com.github.elyspio.swaggercodegen.ui.SwaggerFormData

class KotlinCodegen(info: SwaggerFormData) : JvmCodegen(info) {

    override val dependencyList = listOf("swagger-annotations")
    override val keepFolders = listOf("apis", "models")
    override val additionalProperties = listOf(
        "--library", "jvm-retrofit2",
        "--additional-properties", "dateLibrary=java8,serializationLibrary=gson"
    )
    override val baseFolderPath = listOf("src", "main", "kotlin", "org", "openapitools", "client")

}