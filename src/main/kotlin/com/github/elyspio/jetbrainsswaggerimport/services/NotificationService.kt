package com.github.elyspio.jetbrainsswaggerimport.services

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint


class NotificationService {
    companion object {
        private val STICKY_GROUP = NotificationGroup(
            "demo.notifications.balloon",
            NotificationDisplayType.BALLOON
        )

        fun createNotification(title: String,
            serverity: MessageType = MessageType.INFO
        ) {
            val project = ProjectManager.getInstance().openProjects[0]

            val statusBar: StatusBar = WindowManager.getInstance().getStatusBar(project)

            JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(title, serverity) {}
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.component), Balloon.Position.above)


        }
    }
}