package com.github.elyspio.swaggercodegen.ui

import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.annotations.Nullable
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.util.stream.IntStream
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class DependencyDialog(private val dependencies: Collection<Dependency>) : DialogWrapper(true) {


    class Dependency(val name: String, val fould: Boolean)


    private fun createDependencyUI(dependency: Dependency): Array<JLabel> {
        return arrayOf(
            JLabel(dependency.name),
            JLabel(if (dependency.fould) "Verify" else "Require Install")
        )
    }


    @Nullable
    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(GridBagLayout())

        val leftPanel = JPanel(GridLayout(0, 1))
        val rightPanel = JPanel(GridLayout(0, 1))

        val components = ArrayList<JComponent>()


        this.dependencies.forEach {
            components.addAll(createDependencyUI(it))
        }


        IntStream.range(0, components.size).forEach {

            if (it % 2 == 0) {
                leftPanel.add(components[it])
            } else {
                rightPanel.add(components[it])
            }

        }

        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.BOTH
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 3.0
        dialogPanel.add(leftPanel, gbc)

        gbc.gridx = 1
        gbc.gridy = 0
        gbc.weightx = 20.0
        dialogPanel.add(rightPanel, gbc)

        return dialogPanel
    }

    init {
        init()
        title = "Dependency dialog"
    }


}