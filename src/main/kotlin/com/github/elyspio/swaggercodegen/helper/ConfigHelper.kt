package com.github.elyspio.swaggercodegen.helper

import com.github.elyspio.swaggercodegen.config.AppConfig
import com.github.elyspio.swaggercodegen.config.Versions
import com.github.elyspio.swaggercodegen.helper.swagger.maven.Version
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.io.exists
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ConfigHelper {

    private var config: AppConfig


    val forceDownload: Boolean
        get() = config.forceDownload


    init {

        try {
            if (FileHelper.getConfigPath().exists()) {
                val json = FileHelper.getConfigPath().toFile().readText(Charsets.UTF_8)
                this.config = Json.decodeFromString(json)
                if (config.versions.plugin != getPluginInternalVersion()) {
                    this.config = getDefaultConfig()
                }
            } else {
                this.config = getDefaultConfig()
            }
        } catch (e: SerializationException) {
            this.config = getDefaultConfig()
        }

        write()
    }


    var engineVersion: Version
        get() = config.versions.engine
        set(v) {
            config.versions.engine = v
            write()
        }

    var pluginVersion: Version
        get() = config.versions.plugin
        set(v) {
            config.versions.plugin = v
            write()
        }


    private fun write() {
        FileHelper.getConfigPath().toFile().writeText(Json.encodeToString(this.config))
    }


    private fun getDefaultConfig(): AppConfig {
        return AppConfig(true, Versions(plugin = getPluginInternalVersion()))
    }


    private fun getPluginInternalVersion(): Version {
        return Version(PluginManagerCore.getPlugin(PluginId.getId("com.github.elyspio.swaggercodegen"))!!.version)
    }

}