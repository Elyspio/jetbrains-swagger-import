package com.github.elyspio.jetbrainsswaggerimport.dialog

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.DocumentAdapter
import org.jetbrains.annotations.Nullable
import java.awt.*
import java.util.stream.IntStream
import javax.swing.*


class SwaggerDialog : DialogWrapper(true) {


    var width: Int = 500
    var height: Int = 300

    var output: String = ""
        private set

    var format: String = ""
        private set

    var url: String = ""
        private set


    private fun createOutputFormat(): List<JComponent> {
        val label = JLabel("Output format")
        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)


        val formats = arrayOf<String?>("typescript-axios", "typescript-fetch", "java")

        val formatCombo = ComboBox<String>(formats)

        formatCombo.addActionListener {
            format = formatCombo.selectedItem as String
        }


        formatCombo.size = Dimension(width - 100, 40)
        formatCombo.minimumSize = Dimension(width - 100, 40)

        return listOf(label, formatCombo)
    }

    private fun createOutputFolder(): List<JComponent> {
        val folderLabel = JLabel("Output folder")
        folderLabel.border = BorderFactory.createEmptyBorder(0, 0, 0, 10)

        val button = TextFieldWithBrowseButton()
        val fd = FileChooserDescriptor(false, true, false, false, false, false)
        button.addBrowseFolderListener(TextBrowseFolderListener(fd))
        button.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: javax.swing.event.DocumentEvent) {
                output = button.text
            }
        })
        fd.description = "Folder where files will be generated"

        button.size = Dimension(width - 100, 40)
        button.minimumSize = Dimension(width - 100, 40)

        return listOf(folderLabel, button)
    }

    private fun createInputUrl(): List<JComponent> {
        val panel = JPanel(BorderLayout())
        val label = JLabel("Input url (swagger.json)")
        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        val formatCombo = JFormattedTextField()

        formatCombo.addActionListener {
            url = formatCombo.text
        }

        panel.add(label, BorderLayout.WEST)
        panel.add(formatCombo, BorderLayout.CENTER)

        formatCombo.size = Dimension(width - 100, 40)
        formatCombo.minimumSize = Dimension(width - 100, 40)

        return listOf(panel, formatCombo)

    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(GridBagLayout())

        val leftPanel = JPanel(GridLayout(0, 1))
        val rightPanel = JPanel(GridLayout(0, 1))

        val components = ArrayList<JComponent>()





        createInputUrl().forEach { c -> components.add(c) }
        createOutputFormat().forEach { c -> components.add(c) }
        createOutputFolder().forEach { c -> components.add(c) }

        IntStream.range(0, components.size).forEach {

            if (it % 2 == 0) {
                leftPanel.add(components[it])
            } else {
                rightPanel.add(components[it])
            }

        }

        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.BOTH;
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
        title = "Swagger dialog"
    }
}