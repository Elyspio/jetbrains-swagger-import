package com.github.elyspio.swaggercodegen.ui

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.helper.FileHelper
import com.github.elyspio.swaggercodegen.ui.format.IFormatInput
import com.github.elyspio.swaggercodegen.ui.format.JavaFormatInput
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.DocumentAdapter
import com.intellij.util.containers.stream
import org.jetbrains.annotations.Nullable
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.util.*
import java.util.stream.IntStream
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates


class SwaggerDialog : DialogWrapper(true) {


    var data = SwaggerInfo(this)

    internal var additionalInputs: MutableMap<Format, List<IFormatInput.Data>> = EnumMap(Format::class.java)

    private fun createOutputFormat(): List<JComponent> {
        val label = JLabel("Output format")
        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        val formatCombo = ComboBox(Format.values().stream().map { it.label }.toArray())
        formatCombo.selectedItem = data.format.label
        formatCombo.addActionListener {
            data.format = Format.values().find { f -> f.label == formatCombo.selectedItem } as Format
        }


        formatCombo.size = Dimension(Constants.uiWidth, 40)
        formatCombo.minimumSize = Dimension(Constants.uiWidth, 40)

        return listOf(label, formatCombo)
    }

    private fun createOutputFolder(): List<JComponent> {
        val that = this
        val folderLabel = JLabel("Output folder")
        folderLabel.border = BorderFactory.createEmptyBorder(0, 0, 0, 10)

        val button = TextFieldWithBrowseButton()
        val fd = FileChooserDescriptor(false, true, false, false, false, false)
        button.addBrowseFolderListener(TextBrowseFolderListener(fd))
        button.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                data.output = button.text
                IFormatInput.of(data.format, that)?.onDirectoryChange(data.output)
            }
        })
        fd.description = "Folder where files will be generated"

        button.size = Dimension(Constants.uiWidth, 40)
        button.minimumSize = Dimension(Constants.uiWidth, 40)

        return listOf(folderLabel, button)
    }

    private fun createInputUrl(): List<JComponent> {
        val label = JLabel("Input url (swagger.json)")
        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        val urlField = JTextField()

        urlField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                data.url = urlField.text
            }

            override fun removeUpdate(e: DocumentEvent?) {
                data.url = urlField.text
            }

            override fun changedUpdate(e: DocumentEvent?) {
            }
        })

        urlField.size = Dimension(Constants.uiWidth, 40)
        urlField.minimumSize = Dimension(Constants.uiWidth, 40)

        return listOf(label, urlField)

    }

    private fun createAdditionalPanels() {
        Format.values().forEach {

            if (IFormatInput.of(it, this) != null) {
                additionalInputs[it] = IFormatInput.of(it, this)!!.getComponent()

                if (data.format != it) {
                    additionalInputs[it]?.forEach { c ->
                        c.label.isVisible = false
                        c.input.isVisible = false
                    }
                }
            }
        }
    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(GridBagLayout())

        val leftPanel = JPanel(GridLayout(0, 1))
        val rightPanel = JPanel(GridLayout(0, 1))

        val components = ArrayList<JComponent>()


        createAdditionalPanels()


        createInputUrl().forEach { c -> components.add(c) }
        createOutputFormat().forEach { c -> components.add(c) }
        createOutputFolder().forEach { c -> components.add(c) }



        additionalInputs.forEach { (_, list) -> list.forEach { c -> components.add(c.label); components.add(c.input) } }

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
        title = "Swagger dialog"
    }


    class SwaggerInfo(private var ui: SwaggerDialog) {
        var output: String = ""
            internal set

        var format: Format by Delegates.observable(Format.TypeScriptAxios) { _, oldValue, newValue ->
            run {
                ui.additionalInputs.forEach { entry ->
                    run {
                        if (entry.key == oldValue) {
                            entry.value.forEach { c ->
                                c.input.isVisible = false
                                c.label.isVisible = false
                            }
                        }
                        if (entry.key == newValue) {

                            if (entry.key == Format.JavaRetrofit2 && output.isNotEmpty()) {
                                additionalParams[JavaFormatInput.packagePath] = FileHelper.getPackage(output)
                                (entry.value[0].input as JTextField).text = additionalParams[JavaFormatInput.packagePath] as String

                                additionalParams[JavaFormatInput.gradleBuildLocation] = FileHelper.getGradleBuild(output) ?: ""
                                (entry.value[1].input as TextFieldWithBrowseButton).text = additionalParams[JavaFormatInput.gradleBuildLocation] as String
                            }

                            entry.value.forEach { c ->
                                c.input.isVisible = true
                                c.label.isVisible = true
                            }
                        }
                    }
                }
            }
        }
            internal set


        var url: String = ""
            internal set(value) {
                field = value
                observers.filter { it.key == ObservableProperties.URL }.forEach {
                    it.value.forEach {
                        it(value)
                    }
                }
            }



        var observers: MutableMap<ObservableProperties, MutableList<(value: Any) -> Unit>> = mutableMapOf(Pair(ObservableProperties.URL, mutableListOf()))


        var additionalParams: MutableMap<String, Any> = mutableMapOf()
            internal set


        enum class ObservableProperties(val label: String) {
            URL("URL")
        }


        fun build(): SwaggerFormData {
            return SwaggerFormData(output, url, additionalParams, format)
        }


    }


}

data class SwaggerFormData(
    var output: String,
    var url: String,
    var additionalParams: MutableMap<String, Any>,
    var format: Format
)