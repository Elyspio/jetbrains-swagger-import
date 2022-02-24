package com.github.elyspio.swaggercodegen.ui.common

import com.intellij.openapi.ui.ComboBox
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JComponent
import javax.swing.JList


class AppComboBox<E>(params: Array<E>) : ComboBox<E>(params) {

    private val headerStart = "- "

    init {
        setRenderer(object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(list: JList<*>?, value: Any,
                                                      index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
                if (value is JComponent) return value
                val itemEnabled = !value.toString().startsWith(headerStart)
                super.getListCellRendererComponent(list, value, index,
                        isSelected && itemEnabled, cellHasFocus)
                isEnabled = itemEnabled
                return this
            }
        })
    }

    override fun setSelectedItem(item: Any) {
        if (item.toString().startsWith(headerStart)) return
        super.setSelectedItem(item)
    }
}