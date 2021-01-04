package com.github.elyspio.swaggercodegen.ui

import com.github.elyspio.swaggercodegen.ui.table.DependencyTableModel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBPanel
import com.intellij.ui.table.JBTable
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent


class DependencyDialog(private val dependencies: Collection<Dependency>) : DialogWrapper(true) {


    class Dependency(val name: String, val found: Boolean)

    @Nullable
    override fun createCenterPanel(): JComponent {
        val panel = JBPanel<JBPanel<*>>()

        val table = JBTable()
        table.model = DependencyTableModel(this.dependencies.toList())
        table.columnModel.getColumn(0).preferredWidth = Constants.uiWidth - 150
        table.columnModel.getColumn(1).preferredWidth = 50
        panel.add(table)

        return panel
    }

    init {
        init()
        title = "Dependency dialog"
    }


}