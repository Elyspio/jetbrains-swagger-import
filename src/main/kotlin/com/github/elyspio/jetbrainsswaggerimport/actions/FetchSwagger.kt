package com.github.elyspio.jetbrainsswaggerimport.actions

import com.github.elyspio.jetbrainsswaggerimport.dialog.SwaggerDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FetchSwagger : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {


        SwaggerDialog().showAndGet();
    }
}