package com.github.elyspio.swaggercodegen.ui.format

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import javax.swing.JComponent
import javax.swing.JLabel


interface IFormatInput {
    fun getComponent(): List<Data>
    fun onDirectoryChange(dir: String)
    data class Data(val label: JLabel, val input: JComponent)

    companion object {
        fun of(format: Format, ui: SwaggerDialog): IFormatInput? {
            return when (format) {
                Format.JavaRetrofit2 -> JavaFormatInput(ui)
                Format.Kotlin -> JavaFormatInput(ui)
                Format.KotlinCoroutine -> JavaFormatInput(ui)
                Format.TypeScriptAxios -> null
                Format.TypeScriptFetch -> null
                Format.TypeScriptInversify -> null
                Format.CSharp -> CSharpFormatInput(ui)
                Format.TypeScriptRestTest -> TypeScriptTestRestInput(ui)
            }
        }
    }
}

abstract class FormatInput(val ui: SwaggerDialog) : IFormatInput


