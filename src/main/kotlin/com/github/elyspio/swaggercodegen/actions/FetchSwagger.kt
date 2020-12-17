package com.github.elyspio.swaggercodegen.actions

import com.github.elyspio.swaggercodegen.services.NotificationService
import com.github.elyspio.swaggercodegen.services.SwaggerService
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class FetchSwagger : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = SwaggerDialog()
        if (dialog.showAndGet()) {

            val service = SwaggerService()
            if (service.import(dialog.output, dialog.format, dialog.url)) {
                NotificationService.createNotification("Import succeded")
            } else {
                NotificationService.createNotification("Error while importing Swagger api with url ${dialog.url}")
            }
        }

    }
}