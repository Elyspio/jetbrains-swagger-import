package com.github.elyspio.swaggercodegen.actions

import com.github.elyspio.swaggercodegen.services.NotificationService
import com.github.elyspio.swaggercodegen.services.SwaggerService
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.MessageType


class FetchSwagger : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        val service = SwaggerService()
        val dialog = SwaggerDialog()
        if (dialog.showAndGet()) {
            if (service.generate(dialog.data.build())) {
                NotificationService.createNotification("Import succeded")
            } else {
                NotificationService.createNotification("Error while importing Swagger api with url ${dialog.data.url}", serverity = MessageType.ERROR)
            }
        }


    }
}