package com.github.elyspio.swaggercodegen.helper.swagger


data class SwaggerDefinition(
    val openapi: String,
    val paths: MutableMap<String, MutableList<Operation>>,
    val tags: MutableList<TagElement>
)


data class TagElement(
    val name: String,
)


data class Response(
    val description: String,
    val schema: String?,
    val httpCode: String

)


data class Schema(
    val ref: String
)


data class Operation(
    val operationId: String,
    val responses: MutableList<Response>,
    val method: String,
    var tag: String
)
