package com.github.elyspio.swaggercodegen.ui.table

import com.github.elyspio.swaggercodegen.ui.DependencyDialog
import javax.swing.table.AbstractTableModel

class DependencyTableModel(private val data: List<DependencyDialog.Dependency>) : AbstractTableModel() {

    override fun getColumnName(column: Int): String {
        return columns[column]
    }

    private val columns = listOf("Dependency", "Found")

    override fun getRowCount(): Int {
        return data.size
    }

    override fun getColumnCount(): Int {
        return columns.size;
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val d = data[rowIndex]
        return if (columnIndex == 0) d.name else if (d.found) "V" else "X"
    }
}