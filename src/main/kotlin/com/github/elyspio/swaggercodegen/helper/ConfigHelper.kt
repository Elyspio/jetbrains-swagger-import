package com.github.elyspio.swaggercodegen.helper

import com.github.elyspio.swaggercodegen.config.AppConfig
import com.github.elyspio.swaggercodegen.helper.swagger.maven.Version
import com.intellij.util.io.exists
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ConfigHelper {

    private val config: AppConfig


    val forceDownload: Boolean
        get() = config.forceDownload


    init {

        if (FileHelper.getConfigPath().exists()) {
            val json = FileHelper.getConfigPath().toFile().readText(Charsets.UTF_8)
            this.config = Json.decodeFromString(json)
        } else {
            this.config = AppConfig(Version("0.0.0"), true)
        }

    }


    var version: Version
        get() = config.version
        set(v) {
            config.version = v
            write()
        }


    private fun write() {
        FileHelper.getConfigPath().toFile().writeText(Json.encodeToString(this.config))
    }

}