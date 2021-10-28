package com.github.elyspio.swaggercodegen.config

import com.github.elyspio.swaggercodegen.helper.swagger.maven.Version
import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(val forceDownload: Boolean, var versions: Versions)


@Serializable

data class Versions(var engine: Version = Version.Default, var plugin: Version = Version.Default)
