package com.github.elyspio.swaggercodegen.ui.format

import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.Constants
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.intellij.util.io.exists
import java.awt.Dimension
import java.nio.file.Path
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CSharpFormatInput(ui: SwaggerDialog) : FormatInput(ui) {

    private fun createPackageInput(): List<IFormatInput.Data> {
        val label = JLabel("Csharp namespace")

        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        val field = JTextField()
        field.text = ui.data.additionalParams.csharp.namespace

        field.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                ui.data.additionalParams.csharp.namespace = field.text
            }

            override fun removeUpdate(e: DocumentEvent?) {
                ui.data.additionalParams.csharp.namespace = field.text
            }

            override fun changedUpdate(e: DocumentEvent?) {
            }
        })

        field.size = Dimension(Constants.uiWidth, 40)
        field.minimumSize = Dimension(Constants.uiWidth, 40)

        return listOf(IFormatInput.Data(label, field))
    }


    override fun getComponent(): List<IFormatInput.Data> {

        val list = ArrayList<IFormatInput.Data>()
        list.addAll(createPackageInput())

        return list

    }

    override fun onDirectoryChange(dir: String) {
        if (Path.of(dir).exists()) {
            ui.data.additionalParams.csharp.namespace = FileHelper.getCSharpPackage(dir)
            ((ui.additionalInputs[ui.data.format]?.get(0))?.input as JTextField).text = ui.data.additionalParams.csharp.namespace
        }
    }

}