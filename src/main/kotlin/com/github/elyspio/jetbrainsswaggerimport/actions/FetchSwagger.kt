package com.github.elyspio.jetbrainsswaggerimport.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class FetchSwagger: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse("https://google.fr")
    }
}