package com.github.elyspio.jetbrainsswaggerimport.services

import com.github.elyspio.jetbrainsswaggerimport.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
