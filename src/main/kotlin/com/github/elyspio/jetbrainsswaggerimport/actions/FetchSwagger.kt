package com.github.elyspio.jetbrainsswaggerimport.actions

import com.github.elyspio.jetbrainsswaggerimport.ui.SwaggerDialog
import com.github.elyspio.jetbrainsswaggerimport.services.SwaggerService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FetchSwagger : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        var dialog = SwaggerDialog()

        if (dialog.showAndGet()) {
            var format = dialog.format
            var url = dialog.url
            var folder = dialog.output

            val service = SwaggerService()
            service.import(folder, format, url)

        }

    }
}