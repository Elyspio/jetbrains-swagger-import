package com.github.elyspio.swaggercodegen.ui.format

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.Constants
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.DocumentAdapter
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class JavaFormatInput(ui: SwaggerDialog) : FormatInput(ui) {


    private fun createPackageInput(): List<IFormatInput.Data> {
        val label = JLabel("Java package: ")

        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        val field = JTextField()

        field.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                ui.data.additionalParams["package"] = field.text
            }

            override fun removeUpdate(e: DocumentEvent?) {
                ui.data.additionalParams["package"] = field.text
            }

            override fun changedUpdate(e: DocumentEvent?) {
            }
        })

        field.size = Dimension(Constants.uiWidth, 40)
        field.minimumSize = Dimension(Constants.uiWidth, 40)

        return listOf(
            IFormatInput.Data(label, field)
        )
    }

    private fun createGradlePathInput(): List<IFormatInput.Data> {
        val label = JLabel("Gradle setings location: ")

        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 10)

        val field = TextFieldWithBrowseButton()
        val fd = FileChooserDescriptor(true, false, false, false, false, false)
        field.addBrowseFolderListener(TextBrowseFolderListener(fd))
        field.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                ui.data.additionalParams["gradle-location"] = field.text

            }
        })
        fd.description = "Location of gradle settings"

        field.size = Dimension(Constants.uiWidth, 40)
        field.minimumSize = Dimension(Constants.uiWidth, 40)


        return listOf(
            IFormatInput.Data(label, field)
        )
    }

    override fun getComponent(): List<IFormatInput.Data> {

        val list = ArrayList<IFormatInput.Data>()
        list.addAll(createPackageInput())
        list.addAll(createGradlePathInput())

        return list

    }

    override fun onDirectoryChange(dir: String) {
        ui.data.additionalParams["package"] = FileHelper.getPackage(dir)
        ((ui.additionalInputs[ui.data.format]?.get(0))?.input as JTextField).text = ui.data.additionalParams["package"] as String
    }

}