package com.github.elyspio.swaggercodegen.core

import java.nio.file.Files
import java.nio.file.Path

class GradleDependency(private val pathToGradleBuild: String) {

    fun contains(dependency: String): Boolean {
        val content = Files.readString(Path.of(pathToGradleBuild))
        return content.contains(dependency)
    }

}