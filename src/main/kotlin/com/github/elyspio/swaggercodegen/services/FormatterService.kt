package com.github.elyspio.swaggercodegen.services

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import java.nio.file.Path


object FormatterService {

    fun formatFolder(path: String) {
        // TODO remove the popup memory and local files are different
        val file = Path.of(path).toFile()
        if (!file.isDirectory) throw Exception("The node ${file.absolutePath} is not a directory")

        val files = FileHelper.listFile(path).filter { !it.isDirectory }.map { it.name }

        val project = ProjectManager.getInstance().openProjects[0]

        val fileDocMgr = FileDocumentManager.getInstance()
        val psiDocumentMgr = PsiDocumentManager.getInstance(project)

        ApplicationManager.getApplication().runWriteAction {
            files.forEach {
                FilenameIndex.getFilesByName(project, it, GlobalSearchScope.allScope(project)).forEach { psiFile ->
                    val optimise = OptimizeImportsProcessor(project, psiFile)
                    optimise.runWithoutProgress()
                    println("ran $it")
                    var document = fileDocMgr.getDocument(psiFile.virtualFile)
                    psiDocumentMgr.commitDocument(document!!)
                    document = fileDocMgr.getDocument(psiFile.virtualFile)
                    println("commit $it")
                    fileDocMgr.saveDocument(document!!)
                    println("saved $it")


                }
            }
        }
    }
}
