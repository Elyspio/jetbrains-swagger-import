package com.github.elyspio.swaggercodegen.ui.format

import com.github.elyspio.swaggercodegen.helper.swagger.SwaggerParser
import com.github.elyspio.swaggercodegen.helper.swagger.SwaggerParser.ALL_CONTROLLERS
import com.github.elyspio.swaggercodegen.ui.Constants
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.intellij.openapi.ui.ComboBox
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.JLabel

class TypeScriptTestRestInput(ui: SwaggerDialog) : FormatInput(ui) {


    private var threadSingleton: Thread? = null

    private fun createControllerNameInput(): List<IFormatInput.Data> {
        val label = JLabel("Controller")

        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

//
        val items = arrayOf(ALL_CONTROLLERS)
        val formatCombo = ComboBox(items)
        formatCombo.selectedItem = ALL_CONTROLLERS
        formatCombo.addActionListener {
            val controllers = listOf((formatCombo.selectedItem ?: ALL_CONTROLLERS) as String)
            ui.data.additionalParams.typeScriptTestUnit!!.controllers = controllers
        }

        formatCombo.size = Dimension(Constants.uiWidth, 40)
        formatCombo.minimumSize = Dimension(Constants.uiWidth, 40)



        ui.data.observers[SwaggerDialog.SwaggerInfo.ObservableProperties.URL]?.add { url ->

            threadSingleton?.interrupt()
            threadSingleton = Thread {
                try {
                    formatCombo.removeAllItems()
                    formatCombo.addItem(ALL_CONTROLLERS)
                    val newItems = SwaggerParser.getTags(url.toString()).toTypedArray()
                    for (item in newItems) {
                        formatCombo.addItem(item)
                    }

                } catch (e: Exception) {
                    System.err.println(e.toString())
                }
            }
            threadSingleton!!.start()

        }


        label.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        return listOf(IFormatInput.Data(label, formatCombo))
    }


    override fun getComponent(): List<IFormatInput.Data> {
        return createControllerNameInput()
    }

    override fun onDirectoryChange(dir: String) {
    }


}