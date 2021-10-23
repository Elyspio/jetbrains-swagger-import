package com.github.elyspio.swaggercodegen.helper.swagger.maven

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL


object MavenParser {

    fun getLatest(): Version {
        val json = URL("https://api.factmaven.com/xml-to-json/?xml=https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/maven-metadata.xml").readText()
        val metadata = Json.decodeFromString<MavenMetadata>(json).metadata
        return Version(metadata.versioning.latest)
    }

}