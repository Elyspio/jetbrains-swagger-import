package com.github.elyspio.swaggercodegen.config

import com.github.elyspio.swaggercodegen.helper.swagger.maven.Version
import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(var version: Version, val forceDownload: Boolean)


