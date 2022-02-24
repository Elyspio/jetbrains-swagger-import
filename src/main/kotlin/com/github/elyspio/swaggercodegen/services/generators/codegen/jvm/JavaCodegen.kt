package com.github.elyspio.swaggercodegen.services.generators.codegen.jvm

import com.github.elyspio.swaggercodegen.ui.SwaggerFormData

class JavaCodegen(info: SwaggerFormData) : JvmCodegen(info) {


    override val dependencyList = listOf("swagger-annotations")
    override val keepFolders = listOf("api", "model")
    override val additionalProperties = listOf(
            "--library", "retrofit2",
            "--additional-properties", "java8=true,hideGenerationTimestamp=true,dateLibrary=java8,serializationLibrary=gson"
    )

    override val baseFolderPath = listOf("src", "main", "java", "org", "openapitools", "client")


}