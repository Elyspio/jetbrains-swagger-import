package com.github.elyspio.swaggercodegen.helper.swagger.maven

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MavenMetadata(
    val metadata: Metadata
)

@Serializable
data class Metadata(
    @SerialName("groupId") val groupID: String,

    @SerialName("artifactId") val artifactID: String,

    val versioning: Versioning
)

@Serializable
data class Versioning(
    val latest: String, val release: String, val versions: Versions, val lastUpdated: String
)

@Serializable
data class Versions(
    val version: List<String>
)
