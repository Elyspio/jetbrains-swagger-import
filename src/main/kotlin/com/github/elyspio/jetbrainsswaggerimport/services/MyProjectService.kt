package com.github.elyspio.jetbrainsswaggerimport.services

import com.intellij.openapi.project.Project
import com.github.elyspio.jetbrainsswaggerimport.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
