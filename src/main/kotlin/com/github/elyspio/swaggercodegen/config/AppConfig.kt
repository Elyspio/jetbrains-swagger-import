package com.github.elyspio.swaggercodegen.config

import com.github.elyspio.swaggercodegen.helper.swagger.maven.Version
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(var forceDownload: Boolean, var versions: Versions, var history: SwaggerFormData? = null)


@Serializable

data class Versions(var engine: Version = Version.Default, var plugin: Version = Version.Default)
