package com.github.elyspio.swaggercodegen.actions

import com.github.elyspio.swaggercodegen.services.NotificationService
import com.github.elyspio.swaggercodegen.services.SwaggerService
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.MessageType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FetchSwagger : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        val dialog = SwaggerDialog()
        if (dialog.showAndGet()) {
            GlobalScope.launch {
                val service = SwaggerService()
                if (service.generate(dialog.data.build())) {
                    NotificationService.createNotification("Import succeded")
                } else {
                    NotificationService.createNotification("Error while importing Swagger api with url ${dialog.data.url}", serverity = MessageType.ERROR)
                }
            }.start()
        }


    }
}